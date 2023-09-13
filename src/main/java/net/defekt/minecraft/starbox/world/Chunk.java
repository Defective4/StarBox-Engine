package net.defekt.minecraft.starbox.world;

import net.defekt.minecraft.starbox.MinecraftServer;
import net.defekt.minecraft.starbox.data.Material;
import net.defekt.minecraft.starbox.network.packets.clientbound.play.ServerPlayMultiBlockChangePacket;
import net.defekt.minecraft.starbox.network.packets.clientbound.play.ServerPlayMultiBlockChangePacket.BlockChangeEntry;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Chunk {
    private final World world;
    private final Map<Location, Block> blocks = new ConcurrentHashMap<>();
    private final int x, z;

    private Chunk(World world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    public static Chunk generate(int x, int z, World world) {
        Chunk chk = new Chunk(world, x, z);
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++)
                for (int k = 0; k < 16; k++) {
                    Material type = j == 0 ? Material.BEDROCK : j < 15 ? Material.DIRT : Material.GRASS_BLOCK;
                    int state = j < 15 ? 0 : 1;
                    Location loc = new Location(i, j, k);
                    chk.blocks.put(loc, new Block(loc, type, chk, state));
                }
        return chk;
    }

    public Map<Location, Block> getBlocks() {
        return blocks;
    }

    public Block getBlock(int x, int y, int z) {
        return blocks.getOrDefault(new Location(x, y, z), new Block(new Location(x, y, z), Material.AIR, this, 0));
    }

    public void batchChangeBlocks(Map<Location, BlockState> blocks) {
        Map<Integer, List<BlockChangeEntry>> sectioned = new HashMap<>();
        for (Map.Entry<Location, BlockState> entry : blocks.entrySet()) {
            Location loc = entry.getKey();
            BlockState state = entry.getValue();
            this.blocks.put(loc, new Block(loc, state.getType(), this, state.getStateOffset()));
            int section = Math.floorDiv(loc.getBlockY(), 16);
            if (!sectioned.containsKey(section)) sectioned.put(section, new ArrayList<>());
            BlockChangeEntry cEntry = new BlockChangeEntry(loc.getBlockX(),
                                                           loc.getBlockY() % 16,
                                                           loc.getBlockZ(),
                                                           state.getType().getMinState() + state.getStateOffset());
            sectioned.get(section).add(cEntry);
        }

        for (Map.Entry<Integer, List<BlockChangeEntry>> entry : sectioned.entrySet()) {
            try {
                MinecraftServer.getServer()
                               .broadcastPacket(new ServerPlayMultiBlockChangePacket(getX(),
                                                                                     entry.getKey(),
                                                                                     getZ(),
                                                                                     entry.getValue()
                                                                                          .toArray(new BlockChangeEntry[0])));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setBlock(int x, int y, int z, BlockState state) {
        Location loc = new Location(x, y, z);
        blocks.put(loc, new Block(loc, state.getType(), this, state.getStateOffset()));
        try {
            MinecraftServer.getServer()
                           .broadcastPacket(new ServerPlayMultiBlockChangePacket(getX(),
                                                                                 Math.floorDiv(y, 16),
                                                                                 getZ(),
                                                                                 new BlockChangeEntry(x,
                                                                                                      y % 16,
                                                                                                      z,
                                                                                                      state.getType()
                                                                                                           .getMinState() + state.getStateOffset())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "Chunk{" + "x=" + x + ", z=" + z + '}';
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
