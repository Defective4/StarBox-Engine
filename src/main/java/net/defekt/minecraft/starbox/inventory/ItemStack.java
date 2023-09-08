package net.defekt.minecraft.starbox.inventory;

import dev.dewy.nbt.tags.collection.CompoundTag;
import net.defekt.minecraft.starbox.data.Material;

import java.util.Objects;

public class ItemStack {
    private final Material type;
    private final int count;
    private final CompoundTag nbt;

    public ItemStack(Material type, int count) {
        this.type = type;
        this.count = count;
        nbt = null;
    }

    public ItemStack(Material type) {
        this.type = type;
        nbt = null;
        count = 1;
    }

    public ItemStack(Material type, CompoundTag nbt, int count) {
        Objects.requireNonNull(type);
        this.type = type;
        this.nbt = nbt;
        this.count = count;
    }

    public Material getType() {
        return type;
    }

    public CompoundTag getNbt() {
        return nbt;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "ItemStack{" + "type=" + type + ", count=" + count + ", nbt=" + nbt + '}';
    }
}
