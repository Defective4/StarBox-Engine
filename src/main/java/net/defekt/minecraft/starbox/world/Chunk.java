package net.defekt.minecraft.starbox.world;

import net.defekt.minecraft.starbox.network.PlayerConnection;
import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;
import net.defekt.minecraft.starbox.network.packets.clientbound.play.ServerPlayMultiBlockChangePacket;
import net.defekt.minecraft.starbox.network.packets.clientbound.play.ServerPlayMultiBlockChangePacket.BlockChangeEntry;
import net.defekt.minecraft.starbox.world.generator.ChunkGenerator;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Chunk {
    private final Map<Location, Block> blocks = new ConcurrentHashMap<>();
    private final List<PlayerConnection> viewers = new ArrayList<>();
    private final int x, z;

    public void addViewer(PlayerConnection player) {
        if (!viewers.contains(player)) viewers.add(player);
    }

    public void removeViewer(PlayerConnection player) {
        viewers.remove(player);
    }

    private Chunk(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public void broadcastPacketToViewers(ClientboundPacket packet) {
        for (PlayerConnection viewer : viewers)
            viewer.sendPacket(packet);
    }

    public void batchSetBlocks(Collection<Block> blocks) {
        for (Block block : blocks)
            this.blocks.put(block.getBlockLocation(), block);
        for(Map.Entry<Integer, List<BlockChangeEntry>> entry : convertToProtocol(blocks).entrySet()) {
            try {
                broadcastPacketToViewers(new ServerPlayMultiBlockChangePacket(
                        getX(),
                        entry.getKey(),
                        getZ(), entry.getValue().toArray(new BlockChangeEntry[0])));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Chunk generateChunk(int x, int z, ChunkGenerator generator) {
        Chunk chk = new Chunk(x, z);
        for (Block block : generator.generateChunk(chk)) {
            chk.blocks.put(block.getBlockLocation(), block);
        }
        return chk;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public static Map<Integer, List<BlockChangeEntry>> convertToProtocol(Collection<Block> blocks) {
        Map<Integer, List<BlockChangeEntry>> entries = new HashMap<>();
        for (Block block : blocks) {
            Location loc = block.getBlockLocation();
            int y = Math.floorDiv(loc.getBlockY(), 16);
            if (!entries.containsKey(y)) entries.put(y, new ArrayList<>());
            BlockChangeEntry entry = new BlockChangeEntry(loc.getBlockX(),
                                                          loc.getBlockY() % 16,
                                                          loc.getBlockZ(),
                                                          block.getState());
            entries.get(y).add(entry);
        }
        return entries;
    }

    @Override
    public String toString() {
        return "Chunk{" + "blocks=" + blocks + ", x=" + x + ", z=" + z + '}';
    }

    public Collection<Block> getBlocks() {
        return blocks.values();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chunk chunk = (Chunk) o;
        return x == chunk.x && z == chunk.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }
}
