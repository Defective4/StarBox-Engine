package net.defekt.minecraft.starbox.network.packets.clientbound.status;

import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;

import java.io.IOException;

public class ServerStatusPongPacket extends ClientboundPacket {
    public ServerStatusPongPacket(long payload) throws IOException {
        super(0x01);
        getWrapper().writeLong(payload);
    }
}
