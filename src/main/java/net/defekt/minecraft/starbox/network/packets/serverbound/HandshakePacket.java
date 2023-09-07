package net.defekt.minecraft.starbox.network.packets.serverbound;

import java.io.DataInputStream;
import java.io.IOException;

import static net.defekt.minecraft.starbox.data.CraftDataTypes.readVarInt;
import static net.defekt.minecraft.starbox.data.CraftDataTypes.readVarString;

public class HandshakePacket extends ServerboundPacket {

    private final int protocol, port, state;
    private final String host;

    public HandshakePacket(byte[] data) throws IOException {
        super(data);
        DataInputStream stream = getStream();
        protocol = readVarInt(stream);
        host = readVarString(stream);
        port = stream.readShort();
        state = stream.readByte();
    }

    public int getProtocol() {
        return protocol;
    }

    public int getPort() {
        return port;
    }

    public int getState() {
        return state;
    }

    public String getHost() {
        return host;
    }

    @Override
    public String toString() {
        return "HandshakePacket{" + "protocol=" + protocol + ", port=" + port + ", state=" + state + ", host='" + host + '\'' + '}';
    }
}
