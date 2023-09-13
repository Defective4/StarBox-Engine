package net.defekt.minecraft.starbox.world;

import net.defekt.minecraft.starbox.data.Material;

import java.util.Objects;

public class BlockState {
    private final Material type;
    private final int stateOffset;

    public BlockState(Material type, int stateOffset) {
        this.type = type;
        this.stateOffset = stateOffset;
    }

    public BlockState(Material type) {
        this.type = type;
        stateOffset = 0;
    }

    public Material getType() {
        return type;
    }

    public int getStateOffset() {
        return stateOffset;
    }

    @Override
    public String toString() {
        return "BlockState{" + "type=" + type + ", stateOffset=" + stateOffset + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockState that = (BlockState) o;
        return stateOffset == that.stateOffset && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, stateOffset);
    }
}
