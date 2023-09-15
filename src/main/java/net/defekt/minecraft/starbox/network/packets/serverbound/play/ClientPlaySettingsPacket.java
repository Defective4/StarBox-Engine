package net.defekt.minecraft.starbox.network.packets.serverbound.play;

import net.defekt.minecraft.starbox.data.DataTypes;
import net.defekt.minecraft.starbox.network.packets.serverbound.ServerboundPacket;

import java.io.DataInputStream;
import java.io.IOException;

public class ClientPlaySettingsPacket extends ServerboundPacket {
    private final String lang;
    private final byte renderDistance;

    public ClientPlaySettingsPacket(byte[] data) throws IOException {
        super(data);
        DataInputStream is = getStream();
        String lang = DataTypes.readVarString(is);
        this.lang = lang.substring(0, Math.min(lang.length(), 16));
        renderDistance = is.readByte();
    }

    public String getLang() {
        return lang;
    }

    public byte getRenderDistance() {
        return renderDistance;
    }
}
