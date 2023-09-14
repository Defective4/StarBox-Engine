package net.defekt.minecraft.starbox.world;

import net.defekt.minecraft.starbox.world.generator.ChunkGenerator;
import net.defekt.minecraft.starbox.world.generator.FlatGenerator;
import net.defekt.minecraft.starbox.world.generator.NoiseGenerator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class World {
    private final long seed = System.currentTimeMillis();
    private final Map<Location, Chunk> chunks = new ConcurrentHashMap<>();
    private final ChunkGenerator generator = new NoiseGenerator(this);

    private World() {

    }

    public Chunk getChunkAt(int x, int z) {
        Location loc = new Location(x, 0, z);
        if (!chunks.containsKey(loc)) chunks.put(loc, Chunk.generateChunk(x, z, generator));
        return chunks.get(loc);
    }

    public long getSeed() {
        return seed;
    }

    public static World createWorld() {
        return new World();
    }

    @Override
    public String toString() {
        return "World{" + "seed=" + seed + ", chunks=" + chunks + ", generator=" + generator + '}';
    }
}
