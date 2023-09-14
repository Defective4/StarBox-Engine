package net.defekt.minecraft.starbox.network.packets.serverbound.play;

import net.defekt.minecraft.starbox.network.packets.serverbound.ServerboundPacket;

import java.io.DataInputStream;
import java.io.IOException;

public class ClientPlayPositionPacket extends ServerboundPacket {

    private final double x, y, z;

    public ClientPlayPositionPacket(byte[] data) throws IOException {
        super(data);
        DataInputStream is = getStream();
        x = is.readDouble();
        y = is.readDouble();
        z = is.readDouble();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
