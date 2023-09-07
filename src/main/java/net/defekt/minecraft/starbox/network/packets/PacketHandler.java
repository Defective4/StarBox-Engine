package net.defekt.minecraft.starbox.network.packets;

import net.defekt.minecraft.starbox.network.packets.serverbound.ServerboundPacket;

public interface PacketHandler {
    public void packetReceiving(ServerboundPacket packet);
}
