package net.defekt.minecraft.starbox.world;

import net.defekt.minecraft.starbox.data.Material;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Chunk {
    private final Map<Location, Block> blocks = new ConcurrentHashMap<>();
    private final int x, z;

    private Chunk(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public static Chunk generate(int x, int z) {
        Chunk chk = new Chunk(x, z);
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
        return blocks.getOrDefault(new Location(x, y, z),
                                   new Block(new Location(x, y, z), Material.AIR, this, 0));
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }
}
