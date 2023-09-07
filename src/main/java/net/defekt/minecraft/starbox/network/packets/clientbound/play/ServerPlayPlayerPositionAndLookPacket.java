package net.defekt.minecraft.starbox.network.packets.clientbound.play;

import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;

import java.io.DataOutputStream;
import java.io.IOException;

public class ServerPlayPlayerPositionAndLookPacket extends ClientboundPacket {
    public ServerPlayPlayerPositionAndLookPacket(double x, double y, double z, float yaw, float pitch) throws IOException {
        super(0x34);
        DataOutputStream os = getWrapper();
        os.writeDouble(x);
        os.writeDouble(y);
        os.writeDouble(z);
        os.writeFloat(yaw);
        os.writeFloat(pitch);
        os.writeByte(0);
        os.writeByte(0);
    }
}
