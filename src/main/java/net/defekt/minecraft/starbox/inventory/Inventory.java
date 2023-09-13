package net.defekt.minecraft.starbox.inventory;

import net.defekt.minecraft.starbox.data.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Inventory {
    private final Map<Integer, ItemStack> items = new ConcurrentHashMap<>();
    private final int maxSize;

    protected Inventory(int maxSize) {this.maxSize = maxSize;}

    public ItemStack getItem(int slot) {
        return items.getOrDefault(slot, new ItemStack(Material.AIR));
    }

    public int clear() {
        int size = 0;
        for(ItemStack item : items.values())
            size += item.getCount();
        items.clear();
        return size;
    }

    public void setItem(int slot, ItemStack item) {
        if (slot >= maxSize) return;
        if (item == null || item.getType() == Material.AIR) items.remove(slot);
        else items.put(slot, item);
    }

    public int getSize() {
        return maxSize;
    }

    public Map<Integer, ItemStack> getItems() {
        return new HashMap<>(items);
    }
}
