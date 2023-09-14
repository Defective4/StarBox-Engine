package net.defekt.minecraft.starbox.world.generator;

import net.defekt.minecraft.starbox.data.Material;
import net.defekt.minecraft.starbox.world.Block;
import net.defekt.minecraft.starbox.world.Chunk;
import net.defekt.minecraft.starbox.world.Location;
import net.defekt.minecraft.starbox.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MessGenerator extends ChunkGenerator {
    public MessGenerator(World world) {
        super(world);
    }

    @Override
    public Collection<Block> generateChunk(Chunk chk) {
        List<Block> blocks = new ArrayList<>();
        for (int z = 0; z < 16; z++)
            for (int x = 0; x < 16; x++) {
                Material toSet;
                do {
                    toSet = Material.values()[getRand().nextInt(Material.values().length)];
                } while (!toSet.isBlock());
                blocks.add(new Block(new Location(x, 14, z), toSet, 0));
            }
        return blocks;
    }
}
