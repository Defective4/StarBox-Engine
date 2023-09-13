package net.defekt.minecraft.starbox.world;

import net.defekt.minecraft.starbox.data.Material;

public class Block {
    private final Location location;
    private final Material type;
    private final Chunk chunk;
    private final int blockState;

    public Block(Location location, Material type, Chunk chunk, int blockState) {
        this.location = location;
        this.type = type;
        this.chunk = chunk;
        this.blockState = blockState;
    }

    public Location getLocation() {
        return location;
    }

    public Material getType() {
        return type;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public int getStateOffset() {
        return blockState;
    }

    public BlockState getBlockState() {
        return new BlockState(type, blockState);
    }

    @Override
    public String toString() {
        return "Block{" + "location=" + location + ", type=" + type + ", blockState=" + blockState + '}';
    }
}
