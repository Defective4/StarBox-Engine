package net.defekt.minecraft.starbox.network.packets.clientbound.play;

import net.defekt.minecraft.starbox.data.DataTypes;
import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;

import java.io.DataOutputStream;
import java.io.IOException;

public class ServerPlayPluginMessagePacket extends ClientboundPacket {
    public ServerPlayPluginMessagePacket(String channel, byte[] data) throws IOException {
        super(0x17);
        DataOutputStream wrapper = getWrapper();
        DataTypes.writeVarString(wrapper, channel);
        wrapper.write(data);
    }
}
