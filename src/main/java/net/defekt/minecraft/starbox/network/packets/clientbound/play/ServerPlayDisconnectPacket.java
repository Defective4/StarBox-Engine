package net.defekt.minecraft.starbox.network.packets.clientbound.play;

import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.data.DataTypes;
import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;

import java.io.IOException;

public class ServerPlayDisconnectPacket extends ClientboundPacket {
    public ServerPlayDisconnectPacket(ChatComponent reason) throws IOException {
        super(0x19);
        DataTypes.writeVarString(getWrapper(), reason.toJson());
    }
}
