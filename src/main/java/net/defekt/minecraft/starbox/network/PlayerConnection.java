package net.defekt.minecraft.starbox.network;

import net.defekt.minecraft.starbox.MinecraftServer;
import net.defekt.minecraft.starbox.OpenState;
import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.data.DataTypes;
import net.defekt.minecraft.starbox.inventory.PlayerInventory;
import net.defekt.minecraft.starbox.network.packets.PacketHandler;
import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;
import net.defekt.minecraft.starbox.network.packets.clientbound.play.ServerPlayUpdateLightPacket;
import net.defekt.minecraft.starbox.network.packets.clientbound.play.*;
import net.defekt.minecraft.starbox.network.packets.clientbound.play.ServerPlayMultiBlockChangePacket.BlockChangeEntry;
import net.defekt.minecraft.starbox.network.packets.clientbound.status.ServerStatusResponsePacket;
import net.defekt.minecraft.starbox.network.packets.serverbound.ServerboundPacket;
import net.defekt.minecraft.starbox.world.Chunk;
import net.defekt.minecraft.starbox.world.Location;
import net.defekt.minecraft.starbox.world.World;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;

public class PlayerConnection extends Connection implements AutoCloseable, OpenState {

    private final Socket socket;
    private final DataInputStream inputStream;
    private final OutputStream outputStream;

    private final CorePacketHandler coreHandler;

    private final PlayerInventory inventory = new PlayerInventory(this);

    private GameState gameState = GameState.HANDSHAKING;

    public PlayerConnection(MinecraftServer server, Socket socket) throws IOException {
        super(server);
        this.socket = socket;
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = socket.getOutputStream();
        coreHandler = new CorePacketHandler(this);
    }

    private Location position = new Location(0, 0, 0);

    public Location getPosition() {
        return position;
    }

    protected void setPosition(Location position) {
        this.position = position;
    }

    private final int renderDistance = 10; // TODO
    private final List<Chunk> viewingChunks = new ArrayList<>();

    public void loadTerrain() {
        int cx = Math.floorDiv(position.getBlockX(), 16);
        int cz = Math.floorDiv(position.getBlockZ(), 16);

        int sourceX = cx - renderDistance;
        int sourceZ = cz - renderDistance;
        int targetX = cx + renderDistance;
        int targetZ = cz + renderDistance;

        try {

            for (Chunk loaded : viewingChunks.toArray(new Chunk[0])) {
                int lx = loaded.getX();
                int lz = loaded.getZ();
                if (lx < sourceX || lx > targetX || lz < sourceZ || lz > targetZ) {
                    sendPacket(new ServerPlayUnloadChunkPacket(lx, lz));
                    viewingChunks.remove(loaded);
                }
            }

            sendPacket(new ServerPlayUpdateViewPositionPacket(cx, cz));
            for (int x = sourceX; x <= targetX; x++)
                for (int z = sourceZ; z <= targetZ; z++) {
                    Chunk chk = getWorld().getChunkAt(x, z);
                    if (viewingChunks.contains(chk)) continue;
                    sendPacket(new ServerPlayUpdateLightPacket(x, z));
                    sendPacket(new ServerPlayEmptyChunkPacket(x, z));
                    viewingChunks.add(chk);

                    for (Map.Entry<Integer, List<BlockChangeEntry>> entry : Chunk.convertToProtocol(chk.getBlocks())
                                                                                 .entrySet()) {
                        sendPacket(new ServerPlayMultiBlockChangePacket(x,
                                                                        entry.getKey(),
                                                                        z,
                                                                        entry.getValue()
                                                                             .toArray(new BlockChangeEntry[0])));
                    }

                }
        } catch (Exception e) {
            e.printStackTrace();
            disconnect(ChatComponent.fromString(e.toString()));
        }
    }

    public Collection<PacketHandler> getAllPacketHandlers() {
        return Collections.singleton(coreHandler);
    }

    public PlayerInventory getInventory() {
        return inventory;
    }

    public World getWorld() {
        return getServer().getWorld();
    }

    public void disconnect(ChatComponent reason) {
        try {
            if (gameState == GameState.LOGIN) {
                sendPacket(new ServerStatusResponsePacket(reason.toJson()));
            } else if (gameState == GameState.PLAY) sendPacket(new ServerPlayDisconnectPacket(reason));
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPacket(ClientboundPacket packet) {
        if (isOpen()) {
            try {
                outputStream.write(packet.getData());
            } catch (IOException e) {
                try {
                    disconnect(new ChatComponent.Builder().setText(e.toString()).build());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void handle() throws Exception {
        while (isOpen()) {
            int len = DataTypes.readVarInt(inputStream);
            int id = inputStream.readByte();
            byte[] data = new byte[len - 1];
            inputStream.readFully(data);

            Class<? extends ServerboundPacket> packetClass = ServerboundPacket.Registry.getPacketForID(gameState, id);
            if (packetClass != null) {
                ServerboundPacket packet = packetClass.getConstructor(byte[].class).newInstance(new Object[]{data});
                for (PacketHandler handler : getAllPacketHandlers()) {
                    if (packet.isCancelled()) break;
                    handler.packetReceiving(packet);
                }
            }
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    protected void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public void close() throws Exception {
        socket.close();
    }

    @Override
    public boolean isOpen() {
        return socket.isConnected() && !socket.isClosed();
    }
}
