package net.defekt.minecraft.starbox.world;

import net.defekt.minecraft.starbox.MinecraftServer;
import net.defekt.minecraft.starbox.network.packets.clientbound.play.ServerPlayTimePacket;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class World {
    private final Map<Location, Chunk> chunks = new ConcurrentHashMap<>();

    private final long created = System.currentTimeMillis();
    private long time = 6000;

    public static String formatTime(long time) {
        long t = time + 6000;
        int hours = (int) Math.floorDiv(t, 1000) % 24-1;
        int seconds = (int) (Math.floorMod(t, 1000) * 0.6 / 10) + (hours * 3600);
        return new SimpleDateFormat("HH:ss").format(new Date(seconds * 1000L));
    }

    public long getAge() {
        return (System.currentTimeMillis() - created) / 50;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = Math.abs(time);
        try {
            MinecraftServer.getServer().broadcastPacket(new ServerPlayTimePacket(getAge(), -getTime()));
        } catch (IOException ignored) {}
    }

    private World() {}

    public static World loadWorld() {
        World world = new World();
        for (int x = -2; x <= 2; x++)
            for (int z = -2; z <= 2; z++) {
                world.chunks.put(new Location(x, 0, z), Chunk.generate(x, z, world));
            }
        return world;
    }

    public Block getBlockAt(int x, int y, int z) {
        Chunk chk = getChunkAt(x, z);
        if (chk != null) return chk.getBlock(x % 16, y, z % 16);
        return null;
    }

    public void setBlockAt(int x, int y, int z, BlockState state) {
        Chunk chk = getChunkAt(x, z);
        if (chk != null) {
            chk.setBlock(x % 16, y, z % 16, state);
        }
    }

    public void batchChangeBlocks(Map<Location, BlockState> blocks) {
        Map<Chunk, Map<Location, BlockState>> chunked = new HashMap<>();

        for (Map.Entry<Location, BlockState> entry : blocks.entrySet()) {
            Location loc = entry.getKey();
            Chunk chk = getChunkAt(loc.getBlockX(), loc.getBlockZ());
            if (chk != null) {
                if (!chunked.containsKey(chk)) chunked.put(chk, new HashMap<>());
                chunked.get(chk).put(loc.toChunkLocation(), entry.getValue());
            }
        }

        for (Map.Entry<Chunk, Map<Location, BlockState>> entry : chunked.entrySet())
            entry.getKey().batchChangeBlocks(entry.getValue());
    }

    public Chunk getChunkAt(int x, int z) {
        int cx = Math.floorDiv(x, 16);
        int cz = Math.floorDiv(z, 16);
        return chunks.get(new Location(cx, 0, cz));
    }

    public Collection<Chunk> getChunks() {
        return chunks.values();
    }
}
