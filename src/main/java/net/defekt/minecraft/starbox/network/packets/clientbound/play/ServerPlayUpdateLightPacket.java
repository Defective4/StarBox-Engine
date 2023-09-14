package net.defekt.minecraft.starbox.network.packets.clientbound.play;

import net.defekt.minecraft.starbox.data.DataTypes;
import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static net.defekt.minecraft.starbox.data.DataTypes.writeVarInt;

public class ServerPlayUpdateLightPacket extends ClientboundPacket {
    public ServerPlayUpdateLightPacket(int x, int z) throws IOException {
        super(0x23);
        DataOutputStream wrapper = getWrapper();
        writeVarInt(wrapper, x);
        writeVarInt(wrapper, z);
        wrapper.writeBoolean(true);
        writeVarInt(wrapper, 6);
        writeVarInt(wrapper, 0);
        writeVarInt(wrapper, 1);
        writeVarInt(wrapper, 7);

        byte[] fill = new byte[2048];
        Arrays.fill(fill, (byte) 255);
        writeVarInt(wrapper, 2048);
        wrapper.write(fill);
        writeVarInt(wrapper, 2048);
        wrapper.write(fill);
    }
}
