package net.defekt.minecraft.starbox.network.packets.clientbound.play;

import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;

import java.io.DataOutputStream;
import java.io.IOException;

public class ServerPlayUnloadChunkPacket extends ClientboundPacket {
    public ServerPlayUnloadChunkPacket(int x, int z) throws IOException {
        super(0x1C);
        DataOutputStream wrapper = getWrapper();
        wrapper.writeInt(x);
        wrapper.writeInt(z);
    }
}
