package net.defekt.minecraft.starbox.world.generator;

import net.defekt.minecraft.starbox.data.Material;
import net.defekt.minecraft.starbox.world.Block;
import net.defekt.minecraft.starbox.world.Chunk;
import net.defekt.minecraft.starbox.world.Location;
import net.defekt.minecraft.starbox.world.World;
import net.defekt.minecraft.starbox.world.generator.noise.PerlinOctaveGenerator;
import net.defekt.minecraft.starbox.world.generator.noise.SimplexOctaveGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NoiseGenerator extends ChunkGenerator {

    private final SimplexOctaveGenerator gen;

    public NoiseGenerator(World world) {
        super(world);
        gen = new SimplexOctaveGenerator(world.getSeed(), 8);
        gen.setScale(0.020);
    }

    @Override
    public Collection<Block> generateChunk(Chunk chk) {
        List<Block> blocks = new ArrayList<>();
        for (int x = 0; x < 16; x++)
            for (int z = 0; z < 16; z++) {
                int absX = chk.getX() * 16 + x;
                int absZ = chk.getZ() * 16 + z;
                int y = (int) ((gen.noise(absX, absZ, 0.5, 0.5, true) + 1) * 15 + 4);
                for (int i = 0; i < y; i++) {
                    Material type = i == 0 ?
                            Material.BEDROCK :
                            i == y - 1 ? Material.GRASS_BLOCK : y - i < 5 ? Material.DIRT : Material.STONE;
                    int data = type == Material.GRASS_BLOCK ? 1 : 0;
                    blocks.add(new Block(new Location(x, i, z), type, data));
                }
            }
        return blocks;
    }
}
