package net.defekt.minecraft.starbox.network.packets.clientbound.play;

import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.tags.array.LongArrayTag;
import dev.dewy.nbt.tags.collection.CompoundTag;
import net.defekt.minecraft.starbox.data.DataTypes;
import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ServerPlayEmptyChunkPacket extends ClientboundPacket {
    public ServerPlayEmptyChunkPacket(int x, int z) throws IOException {
        super(0x20);
        DataOutputStream wrapper = getWrapper();
        wrapper.writeInt(x);
        wrapper.writeInt(z);
        wrapper.writeBoolean(true);
        wrapper.writeByte(0);

        CompoundTag map = new CompoundTag();
        map.put("MOTION_BLOCKING", new LongArrayTag(new long[36]));
        map.put("WORLD_SURFACE", new LongArrayTag(new long[36]));
        new Nbt().toStream(map, wrapper);

        int[] biomeIDs = new int[1024];
        Arrays.fill(biomeIDs, 1);

        DataTypes.writeVarInt(wrapper, biomeIDs.length);
        for (int id : biomeIDs)
            DataTypes.writeVarInt(wrapper, id);
        wrapper.writeByte(0);
        wrapper.writeByte(0);
    }
}
