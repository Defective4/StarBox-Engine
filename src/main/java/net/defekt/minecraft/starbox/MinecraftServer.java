package net.defekt.minecraft.starbox;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.api.registry.TagTypeRegistry;
import dev.dewy.nbt.tags.collection.CompoundTag;
import net.defekt.minecraft.starbox.command.CommandRegistry;
import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.data.PlayerProfile;
import net.defekt.minecraft.starbox.network.Connection;
import net.defekt.minecraft.starbox.network.PlayerConnection;
import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;
import net.defekt.minecraft.starbox.network.packets.clientbound.play.ServerPlayKeepAlivePacket;
import net.defekt.minecraft.starbox.network.packets.clientbound.play.ServerPlayPlayerInfoPacket;
import net.defekt.minecraft.starbox.world.World;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MinecraftServer implements AutoCloseable, OpenState {

    public static final int PROTOCOL = 754;
    public static final String VERSION = "StarBox 1.16.5";

    private final ServerSocket srv;
    private final ExecutorService pool = Executors.newCachedThreadPool();

    private final CompoundTag dimensionCodec;

    private final Map<UUID, Connection> onlineConnections = new ConcurrentHashMap<>();
    private final Timer timer = new Timer(true);

    private final World world;

    private final CommandRegistry commandRegistry = new CommandRegistry(this);

    private static MinecraftServer server;

    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    public static MinecraftServer getServer() {
        return server;
    }

    public MinecraftServer(String host, int port) throws IOException {
        server = this;
        srv = host == null ? new ServerSocket(port) : new ServerSocket(port, 50, InetAddress.getByName(host));

        world = World.loadWorld();

        File codecFile = new File("codec.json");
        CompoundTag codec = null;
        CompoundTag def;
        Nbt nbt = new Nbt();
        try (DataInputStream in = new DataInputStream(getClass().getResourceAsStream("/codec.bin"))) {
            def = nbt.fromStream(in);
        }

        if (!codecFile.exists()) {
            try (OutputStream os = Files.newOutputStream(codecFile.toPath())) {
                JsonObject json = def.toJson(0, new TagTypeRegistry());
                os.write(new GsonBuilder().setPrettyPrinting().create().toJson(json).getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (codecFile.isFile()) {
            try {
                codec = nbt.fromJson(codecFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        dimensionCodec = codec == null ? def : codec;

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (Connection con : getOnlineConnections()) {
                    try {
                        con.sendPacket(new ServerPlayKeepAlivePacket());
                    } catch (IOException ignored) {}
                }
            }
        }, 5000, 5000);
    }

    public World getWorld() {
        return world;
    }

    public void broadcastPacket(ClientboundPacket packet) {
        for (Connection con : getOnlineConnections())
            try {
                con.sendPacket(packet);
            } catch (Exception ignored) {}
    }

    public Collection<Connection> getOnlineConnections() {
        return new ArrayList<>(onlineConnections.values());
    }

    public void insertConnection(Connection con) {
        if (con.getProfile() == null) return;
        onlineConnections.put(con.getProfile().getUuid(), con);
        try {
            broadcastPacket(new ServerPlayPlayerInfoPacket(ServerPlayPlayerInfoPacket.Action.ADD_PLAYER,
                                                           con.getProfile()));
            broadcastMessage(new ChatComponent.Builder().setColor("yellow")
                                                        .setTranslate("multiplayer.player.joined")
                                                        .addWith(ChatComponent.fromString(con.getProfile().getName()))
                                                        .build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PlayerConnection getPlayer(String name) {
        for (Connection con : getOnlineConnections())
            if (con instanceof PlayerConnection && con.getProfile().getName().equalsIgnoreCase(name))
                return (PlayerConnection) con;
        return null;
    }

    public PlayerConnection getPlayer(UUID uid) {
        for (Connection con : getOnlineConnections())
            if (con instanceof PlayerConnection && con.getProfile().getUuid().equals(uid))
                return (PlayerConnection) con;
        return null;
    }

    public Connection getConnection(String name) {
        for (Connection con : getOnlineConnections())
            if (con.getProfile().getName().equalsIgnoreCase(name)) return con;
        return null;
    }

    public Connection getConnection(UUID uid) {
        for (Connection con : getOnlineConnections())
            if (con.getProfile().getUuid().equals(uid)) return con;
        return null;
    }

    public void broadcastMessage(ChatComponent message) {
        for (Connection con : getOnlineConnections()) {
            con.sendMessage(message);
        }
    }

    public CompoundTag getDimensionCodec() {
        return dimensionCodec;
    }

    public void start() {
        while (isOpen()) {
            try {
                Socket client = srv.accept();
                pool.submit(() -> {
                    PlayerConnection lCon = null;
                    try (PlayerConnection con = new PlayerConnection(this, client)) {
                        lCon = con;
                        con.handle();
                    } catch (EOFException ignored) {} catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        PlayerProfile prof = lCon.getProfile();
                        if (prof != null) {
                            onlineConnections.remove(prof.getUuid());
                            try {
                                broadcastPacket(new ServerPlayPlayerInfoPacket(ServerPlayPlayerInfoPacket.Action.REMOVE_PLAYER,
                                                                               prof));
                                broadcastMessage(new ChatComponent.Builder().setColor("yellow")
                                                                            .setTranslate("multiplayer.player.left")
                                                                            .addWith(ChatComponent.fromString(prof.getName()))
                                                                            .build());
                            } catch (IOException ignored) {}
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getCurrentStatus() {
        JsonObject root = new JsonObject();

        JsonObject version = new JsonObject();
        version.add("name", new JsonPrimitive(VERSION));
        version.add("protocol", new JsonPrimitive(PROTOCOL));

        JsonObject players = new JsonObject();
        players.add("max", new JsonPrimitive(1));
        players.add("online", new JsonPrimitive(onlineConnections.size()));
        players.add("sample", new JsonArray());

        JsonObject description = new JsonObject();
        description.add("text", new JsonPrimitive("A StarBox server"));

        root.add("version", version);
        root.add("players", players);
        root.add("description", description);
        return root.toString();
    }

    @Override
    public void close() throws Exception {
        srv.close();
    }

    @Override
    public boolean isOpen() {
        return srv.isBound() && !srv.isClosed();
    }
}
