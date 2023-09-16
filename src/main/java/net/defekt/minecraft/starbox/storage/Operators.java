package net.defekt.minecraft.starbox.storage;

import net.defekt.minecraft.starbox.MinecraftServer;
import net.defekt.minecraft.starbox.network.PlayerConnection;
import net.defekt.minecraft.starbox.network.packets.clientbound.play.ServerPlayEntityStatusPacket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Operators {
    private final List<String> ops = new ArrayList<>();

    public boolean op(String player) {
        player = player.toLowerCase();
        if (ops.contains(player)) return false;
        ops.add(player);
        PlayerConnection con = MinecraftServer.getServer().getPlayer(player);
        if (con != null) {
            try {
                con.sendPacket(new ServerPlayEntityStatusPacket(con.getEntityID(), 28));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean deop(String player) {
        player = player.toLowerCase();
        boolean removed = ops.remove(player);
        if (removed) {
            PlayerConnection con = MinecraftServer.getServer().getPlayer(player);
            if (con != null) try {
                con.sendPacket(new ServerPlayEntityStatusPacket(con.getEntityID(), 24));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public boolean isOp(String player) {
        return ops.contains(player.toLowerCase());
    }
}
