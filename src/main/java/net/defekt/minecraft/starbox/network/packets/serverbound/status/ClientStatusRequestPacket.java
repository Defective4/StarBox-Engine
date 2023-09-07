package net.defekt.minecraft.starbox.network.packets.serverbound.status;

import net.defekt.minecraft.starbox.network.packets.serverbound.ServerboundPacket;

import java.io.IOException;

public class ClientStatusRequestPacket extends ServerboundPacket {
    public ClientStatusRequestPacket(byte[] data) throws IOException {
        super(data);
    }
}
