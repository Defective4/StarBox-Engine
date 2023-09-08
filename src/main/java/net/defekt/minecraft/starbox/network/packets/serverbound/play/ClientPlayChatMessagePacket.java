package net.defekt.minecraft.starbox.network.packets.serverbound.play;

import net.defekt.minecraft.starbox.data.DataTypes;
import net.defekt.minecraft.starbox.network.packets.serverbound.ServerboundPacket;

import java.io.IOException;

public class ClientPlayChatMessagePacket extends ServerboundPacket {

    private final String message;

    public ClientPlayChatMessagePacket(byte[] data) throws IOException {
        super(data);
        message = DataTypes.readVarString(getStream());
    }

    public String getMessage() {
        return message;
    }
}
