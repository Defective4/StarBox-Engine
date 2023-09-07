package net.defekt.minecraft.starbox.network.packets.serverbound.login;

import net.defekt.minecraft.starbox.data.DataTypes;
import net.defekt.minecraft.starbox.network.packets.serverbound.ServerboundPacket;

import java.io.IOException;

public class ClientLoginStartPacket extends ServerboundPacket {

    private final String name;

    public ClientLoginStartPacket(byte[] data) throws IOException {
        super(data);
        name = DataTypes.readVarString(getStream());
    }

    public String getName() {
        return name;
    }
}
