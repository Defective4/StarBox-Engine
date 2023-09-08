package net.defekt.minecraft.starbox.world;

import net.defekt.minecraft.starbox.data.Material;

public class Block {
    private final Location location;
    private final Material type;
    private final Chunk chunk;

    public Block(Location location, Material type, Chunk chunk) {
        this.location = location;
        this.type = type;
        this.chunk = chunk;
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
}
