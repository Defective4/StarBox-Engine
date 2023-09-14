package net.defekt.minecraft.starbox.network.packets.clientbound.play;

import net.defekt.minecraft.starbox.command.Command;
import net.defekt.minecraft.starbox.data.DataTypes;
import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;

import java.io.DataOutputStream;
import java.io.IOException;

public class ServerPlayDeclareCommandsPacket extends ClientboundPacket {
    public ServerPlayDeclareCommandsPacket(Command... cmds) throws IOException {
        super(0x10);
        DataOutputStream wrapper = getWrapper();
        DataTypes.writeVarInt(wrapper, cmds.length + 1);
        wrapper.writeByte(0x00);
        DataTypes.writeVarInt(wrapper, cmds.length);
        for (int x = 0; x < cmds.length; x++)
            DataTypes.writeVarInt(wrapper, x + 1);

        for (Command cmd : cmds) {
            wrapper.writeByte(0x01 | 0x04);
            wrapper.writeByte(0);
            DataTypes.writeVarString(wrapper, cmd.getName());
        }

        wrapper.writeByte(0);
    }
}
