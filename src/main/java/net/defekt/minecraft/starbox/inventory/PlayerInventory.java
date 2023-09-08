package net.defekt.minecraft.starbox.inventory;

import net.defekt.minecraft.starbox.network.PlayerConnection;
import net.defekt.minecraft.starbox.network.packets.clientbound.play.ServerPlaySetSlotPacket;

public class PlayerInventory extends Inventory {

    private final PlayerConnection player;

    public PlayerInventory(PlayerConnection player) {
        super(36);
        this.player = player;
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
