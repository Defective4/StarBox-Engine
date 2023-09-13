package net.defekt.minecraft.starbox.inventory;

import net.defekt.minecraft.starbox.network.PlayerConnection;
import net.defekt.minecraft.starbox.network.packets.clientbound.play.ServerPlaySetSlotPacket;
import net.defekt.minecraft.starbox.network.packets.clientbound.play.ServerPlayWindowItemsPacket;

import java.io.IOException;

public class PlayerInventory extends Inventory {

    private final PlayerConnection player;

    public PlayerInventory(PlayerConnection player) {
        super(46);
        setReservedSlots(9);
        this.player = player;
    }

    @Override
    public int clear() {
        int cleared = super.clear();
        try {
            player.sendPacket(new ServerPlayWindowItemsPacket(0, new ItemStack[getSize()]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cleared;
    }

    public PlayerConnection getPlayer() {
        return player;
    }

    @Override
    public void setItem(int slot, ItemStack item) {
        super.setItem(slot, item);
        try {
            player.sendPacket(new ServerPlaySetSlotPacket(0, slot, item));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
