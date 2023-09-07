package net.defekt.minecraft.starbox.network.packets.clientbound.status;

import net.defekt.minecraft.starbox.data.CraftDataTypes;
import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;

import java.io.DataOutputStream;
import java.io.IOException;

public class ServerStatusResponsePacket extends ClientboundPacket {
    public ServerStatusResponsePacket(String response) throws IOException {
        super(0x00);
        DataOutputStream os = getWrapper();
        CraftDataTypes.writeVarString(os, response);
    }
}
