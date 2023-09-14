package net.defekt.minecraft.starbox.network.packets.clientbound.play;

import net.defekt.minecraft.starbox.data.DataTypes;
import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;

import java.io.DataOutputStream;
import java.io.IOException;

public class ServerPlayUpdateViewPositionPacket extends ClientboundPacket {
    public ServerPlayUpdateViewPositionPacket(int x, int z) throws IOException {
        super(0x40);
        DataOutputStream wrapper = getWrapper();
        DataTypes.writeVarInt(wrapper, x);
        DataTypes.writeVarInt(wrapper, z);
    }
}
