package net.defekt.minecraft.starbox.world;

import net.defekt.minecraft.starbox.data.Material;

import java.util.Objects;

public class Block {
    private final Location blockLocation;
    private final Material type;
    private final int stateOffset;

    public Block(Location blockLocation, Material type) {
        this(blockLocation, type, 0);
    }

    public Block(Location blockLocation, Material type, int stateOffset) {
        Objects.requireNonNull(blockLocation);
        Objects.requireNonNull(type);
        if (!type.isBlock()) throw new IllegalArgumentException("Material must be a block type!");
        this.blockLocation = blockLocation;
        this.type = type;
        this.stateOffset = stateOffset;
    }

    public int getState() {
        return Math.min(type.getMinState() + stateOffset, type.getMaxState());
    }

    public Location getBlockLocation() {
        return blockLocation;
    }

    public Material getType() {
        return type;
    }

    public int getStateOffset() {
        return stateOffset;
    }

    @Override
    public String toString() {
        return "Block{" + "blockLocation=" + blockLocation + ", type=" + type + ", stateOffset=" + stateOffset + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return stateOffset == block.stateOffset && Objects.equals(blockLocation,
                                                                  block.blockLocation) && type == block.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockLocation, type, stateOffset);
    }
}
