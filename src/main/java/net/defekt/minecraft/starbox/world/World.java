package net.defekt.minecraft.starbox.world;

import net.defekt.minecraft.starbox.MinecraftServer;
import net.defekt.minecraft.starbox.network.PlayerConnection;
import net.defekt.minecraft.starbox.network.packets.clientbound.play.ServerPlayTimePacket;
import net.defekt.minecraft.starbox.world.generator.ChunkGenerator;
import net.defekt.minecraft.starbox.world.generator.FlatGenerator;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class World {

    private int time = 6000;
    private final long created = System.currentTimeMillis();

    private final long seed = System.currentTimeMillis();
    private final Map<Location, Chunk> chunks = new ConcurrentHashMap<>();
    private final ChunkGenerator generator = new FlatGenerator(this);

    public long getAgeTicks() {
        return (System.currentTimeMillis() - created) / 50;
    }

    public int getTime() {
        return time % 24000;
    }

    public String getParsedTime() {
        int time = ((this.time + 6000) % 24000);
        int hours = time / 1000;
        int seconds = (int) (((double) (time % 1000) / 1000) * 60);

        long hourM = TimeUnit.HOURS.toMillis(1);
        return new SimpleDateFormat("HH:ss").format(new Date(hours * hourM + seconds * 1000L - hourM));
    }

    public void setTime(int time) {
        this.time = Math.abs(time % 24000);
        try {
            MinecraftServer.getServer().broadcastPacket(new ServerPlayTimePacket(getAgeTicks(), -time));
        } catch (IOException ignored) {}
    }

    private World() {}

    public static World createWorld() {
        return new World();
    }

    public void batchFillBlocks(Map<Location, BlockState> blocks) {
        Map<Chunk, List<Block>> chunked = new HashMap<>();
        for (Map.Entry<Location, BlockState> entry : blocks.entrySet()) {
            Location abs = entry.getKey();
            Location loc = abs.toChunkLocation();
            Chunk chk = getChunkAt(loc.getBlockX(), loc.getBlockZ());
            if (!chunked.containsKey(chk)) chunked.put(chk, new ArrayList<>());
            BlockState state = entry.getValue();
            chunked.get(chk)
                   .add(new Block(new Location(abs.getBlockX() % 16, abs.getBlockY(), abs.getBlockZ() % 16),
                                  state.getType(),
                                  state.getOffset()));
        }

        for (Map.Entry<Chunk, List<Block>> entry : chunked.entrySet()) {
            entry.getKey().batchSetBlocks(entry.getValue());
        }
    }

    public Chunk getChunkAt(int x, int z) {
        Location loc = new Location(x, 0, z);
        if (!chunks.containsKey(loc)) chunks.put(loc, Chunk.generateChunk(x, z, generator));
        return chunks.get(loc);
    }

    public long getSeed() {
        return seed;
    }

    @Override
    public String toString() {
        return "World{" + "seed=" + seed + ", chunks=" + chunks + ", generator=" + generator + '}';
    }

    public void dropViewer(PlayerConnection player) {
        for (Chunk chk : player.getViewingChunks()) {
            chk.removeViewer(player);
            reviewChunk(new Location(chk.getX(), 0, chk.getZ()));
        }
        for (Chunk chk : chunks.values())
            chk.removeViewer(player);
    }

    public void reviewChunk(Location loc) {
        Chunk chk = getChunkAt(loc.getBlockX(), loc.getBlockZ());
        if (!chk.hasViewers()) dropChunk(loc);
    }

    public void dropChunk(Location loc) {
        chunks.remove(loc);
    }
}
