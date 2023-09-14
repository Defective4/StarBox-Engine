package net.defekt.minecraft.starbox.network.packets.clientbound.play;

import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;
import net.defekt.minecraft.starbox.world.Location;

import java.io.DataOutputStream;
import java.io.IOException;

public class ServerPlayPlayerPositionAndLookPacket extends ClientboundPacket {
    public ServerPlayPlayerPositionAndLookPacket(Location loc) throws IOException {
        super(0x34);
        DataOutputStream os = getWrapper();
        os.writeDouble(loc.getX());
        os.writeDouble(loc.getY());
        os.writeDouble(loc.getZ());
        os.writeFloat(loc.getYaw());
        os.writeFloat(loc.getPitch());
        os.writeByte(0);
        os.writeByte(0);
    }
}
