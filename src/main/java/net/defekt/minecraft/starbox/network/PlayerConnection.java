package net.defekt.minecraft.starbox.network;

import net.defekt.minecraft.starbox.MinecraftServer;
import net.defekt.minecraft.starbox.OpenState;
import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.data.DataTypes;
import net.defekt.minecraft.starbox.network.packets.PacketHandler;
import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;
import net.defekt.minecraft.starbox.network.packets.clientbound.status.ServerStatusResponsePacket;
import net.defekt.minecraft.starbox.network.packets.serverbound.ServerboundPacket;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Collections;

public class PlayerConnection extends Connection implements AutoCloseable, OpenState {

    private final Socket socket;
    private final DataInputStream inputStream;
    private final OutputStream outputStream;

    private final CorePacketHandler coreHandler;
    private GameState gameState = GameState.HANDSHAKING;

    public PlayerConnection(MinecraftServer server, Socket socket) throws IOException {
        super(server);
        this.socket = socket;
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = socket.getOutputStream();
        coreHandler = new CorePacketHandler(this);
    }

    public Collection<PacketHandler> getAllPacketHandlers() {
        return Collections.singleton(coreHandler);
    }

    public void disconnect(ChatComponent reason) throws Exception {
        try {
            if (gameState == GameState.LOGIN) {
                sendPacket(new ServerStatusResponsePacket(reason.toJson()));
            }
        } catch (Exception ignored) {}
        close();
    }

    @Override
    public void sendPacket(ClientboundPacket packet) throws IOException {
        if (isOpen()) outputStream.write(packet.getData());
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
