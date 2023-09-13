package net.defekt.minecraft.starbox.network.packets.clientbound.play;

import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;

import java.io.DataOutputStream;
import java.io.IOException;

public class ServerPlayTimePacket extends ClientboundPacket {
    public ServerPlayTimePacket(long age, long time) throws IOException {
        super(0x4E);
        DataOutputStream wrapper = getWrapper();
        wrapper.writeLong(age);
        wrapper.writeLong(time);
    }
}
