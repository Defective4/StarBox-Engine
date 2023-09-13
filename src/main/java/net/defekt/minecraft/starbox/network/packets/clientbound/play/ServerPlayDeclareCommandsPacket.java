package net.defekt.minecraft.starbox.network.packets.clientbound.play;

import net.defekt.minecraft.starbox.command.Command;
import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;

import java.io.IOException;

public class ServerPlayDeclareCommandsPacket extends ClientboundPacket {
    public ServerPlayDeclareCommandsPacket(Command... cmds) throws IOException {
        super(0x10);
        //TODO
    }
}
