package net.defekt.minecraft.starbox.world.generator;

import net.defekt.minecraft.starbox.world.Block;
import net.defekt.minecraft.starbox.world.Chunk;
import net.defekt.minecraft.starbox.world.World;

import java.util.Collection;
import java.util.Random;

public abstract class ChunkGenerator {

    private final World world;
    private final Random rand;

    protected ChunkGenerator(World world) {
        this.world = world;
        rand = new Random(world.getSeed());
    }

    public World getWorld() {
        return world;
    }

    public abstract Collection<Block> generateChunk(Chunk chk);


    public Random getRand() {
        return rand;
    }
}
