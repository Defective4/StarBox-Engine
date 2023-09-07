package net.defekt.minecraft.starbox.network.packets.serverbound.status;

import net.defekt.minecraft.starbox.network.packets.serverbound.ServerboundPacket;

import java.io.IOException;

public class ClientStatusPingPacket extends ServerboundPacket {

    private final long payload;

    public ClientStatusPingPacket(byte[] data) throws IOException {
        super(data);
        payload = getStream().readLong();
    }

    public long getPayload() {
        return payload;
    }
}
