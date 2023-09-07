package net.defekt.minecraft.starbox.network.packets.clientbound.play;

import net.defekt.minecraft.starbox.data.DataTypes;
import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;

import java.io.DataOutputStream;
import java.io.IOException;

public class ServerPlayMultiBlockChangePacket extends ClientboundPacket {

    public static class BlockChangeEntry {
        private final int x, y, z, id;

        public BlockChangeEntry(int x, int y, int z, int id) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.id = id;
        }

        public long getEncoded() {
            return (long) id << 12 | ((long) x << 8 | (long) z << 4 | y);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }

        public int getId() {
            return id;
        }
    }

    public ServerPlayMultiBlockChangePacket(int cx, int cy, int cz, BlockChangeEntry... blocks) throws IOException {
        super(0x3B);
        DataOutputStream wrapper = getWrapper();
        wrapper.writeLong(((long) (cx & 0x3FFFFF) << 42) | (cy & 0xFFFFF) | ((long) (cz & 0x3FFFFF) << 20));
        wrapper.writeBoolean(false);
        DataTypes.writeVarInt(wrapper, blocks.length);
        for (BlockChangeEntry entry : blocks) {
            DataTypes.writeVarLong(wrapper, entry.getEncoded());
        }
    }
}
