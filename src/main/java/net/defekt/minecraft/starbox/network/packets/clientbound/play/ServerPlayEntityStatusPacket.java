package net.defekt.minecraft.starbox.network.packets.clientbound.play;

import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;

import java.io.DataOutputStream;
import java.io.IOException;

public class ServerPlayEntityStatusPacket extends ClientboundPacket {

    public ServerPlayEntityStatusPacket(int id, int status) throws IOException {
        super(0x1A);
        DataOutputStream wrapper = getWrapper();
        wrapper.writeInt(id);
        wrapper.writeByte(status);
    }
}
