package net.defekt.minecraft.starbox.network.packets.clientbound.play;

import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;

import java.io.IOException;

public class ServerPlayKeepAlivePacket extends ClientboundPacket {
    public ServerPlayKeepAlivePacket() throws IOException {
        super(0x1F);
        getWrapper().writeLong(System.currentTimeMillis());
    }
}
