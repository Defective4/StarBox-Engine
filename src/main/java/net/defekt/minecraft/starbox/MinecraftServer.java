package net.defekt.minecraft.starbox;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.defekt.minecraft.starbox.network.PlayerConnection;

import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MinecraftServer implements AutoCloseable, OpenState {

    public static final int PROTOCOL = 754;
    public static final String VERSION = "StarBox 1.16.5";

    private final ServerSocket srv;
    private final ExecutorService pool = Executors.newCachedThreadPool();

    public MinecraftServer(String host, int port) throws IOException {
        srv = host == null ? new ServerSocket(port) : new ServerSocket(port, 50, InetAddress.getByName(host));
    }

    public void start() {
        while (isOpen()) {
            try {
                Socket client = srv.accept();
                pool.submit(() -> {
                    try (PlayerConnection con = new PlayerConnection(this, client)) {
                        con.handle();
                    } catch (EOFException ignored) {} catch (Exception e) {
                        e.printStackTrace();
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
        players.add("online", new JsonPrimitive(0));
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
