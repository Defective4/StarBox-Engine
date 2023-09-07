package net.defekt.minecraft.starbox.network.packets.clientbound.login;

import net.defekt.minecraft.starbox.data.CraftDataTypes;
import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class ServerLoginSuccessPacket extends ClientboundPacket {
    public ServerLoginSuccessPacket(UUID uid, String name) throws IOException {
        super(0x02);
        DataOutputStream os = getWrapper();
        CraftDataTypes.writeUUID(os, uid);
        CraftDataTypes.writeVarString(os, name);
    }
}
