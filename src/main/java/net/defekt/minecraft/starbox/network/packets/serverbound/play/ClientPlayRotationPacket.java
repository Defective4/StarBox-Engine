package net.defekt.minecraft.starbox.network.packets.serverbound.play;

import net.defekt.minecraft.starbox.network.packets.serverbound.ServerboundPacket;

import java.io.DataInputStream;
import java.io.IOException;

public class ClientPlayRotationPacket extends ServerboundPacket {

    private final float yaw, pitch;

    public ClientPlayRotationPacket(byte[] data) throws IOException {
        super(data);
        DataInputStream is = getStream();
        yaw = is.readFloat();
        pitch = is.readFloat();
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }
}
