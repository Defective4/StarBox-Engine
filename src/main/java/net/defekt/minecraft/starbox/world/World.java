package net.defekt.minecraft.starbox.world;

import java.util.ArrayList;
import java.util.List;

public class World {
    private final List<Chunk> chunks = new ArrayList<>();

    private World() {}

    public static World loadWorld() {
        World world = new World();
        for (int x = -10; x <= 10; x++)
            for (int z = -10; z <= 10; z++)
                world.chunks.add(Chunk.generate(x, z));
        return world;
    }

    public List<Chunk> getChunks() {
        return chunks;
    }
}
