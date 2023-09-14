package net.defekt.minecraft.starbox.network.packets.serverbound.play;

import net.defekt.minecraft.starbox.network.packets.serverbound.ServerboundPacket;
import net.defekt.minecraft.starbox.world.Location;

import java.io.DataInputStream;
import java.io.IOException;

public class ClientPlayPositionAndRotationPacket extends ServerboundPacket {

    private final double x, y, z;
    private final float yaw, pitch;

    public ClientPlayPositionAndRotationPacket(byte[] data) throws IOException {
        super(data);
        DataInputStream is = getStream();
        x = is.readDouble();
        y = is.readDouble();
        z = is.readDouble();
        yaw = is.readFloat();
        pitch = is.readFloat();
    }

    public Location getLocation() {
        return new Location(x, y, z, yaw, pitch);
    }
}
