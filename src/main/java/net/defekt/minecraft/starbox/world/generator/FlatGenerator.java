package net.defekt.minecraft.starbox.world.generator;

import net.defekt.minecraft.starbox.data.Material;
import net.defekt.minecraft.starbox.world.Block;
import net.defekt.minecraft.starbox.world.Chunk;
import net.defekt.minecraft.starbox.world.Location;
import net.defekt.minecraft.starbox.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class FlatGenerator extends ChunkGenerator {
    public FlatGenerator(World world) {super(world);}

    @Override
    public Collection<Block> generateChunk(Chunk chk) {
        List<Block> blocks = new ArrayList<>();
        for (int x = 0; x < 16; x++)
            for (int y = 0; y < 4; y++)
                for (int z = 0; z < 16; z++) {
                    Material type = y == 0 ? Material.BEDROCK : y == 3 ? Material.GRASS_BLOCK : Material.DIRT;
                    blocks.add(new Block(new Location(x, y, z), type, type == Material.GRASS_BLOCK ? 1 : 0));
                }
        return blocks;
    }
}
