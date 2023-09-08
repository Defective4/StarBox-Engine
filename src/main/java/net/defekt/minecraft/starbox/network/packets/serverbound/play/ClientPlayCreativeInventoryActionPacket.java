package net.defekt.minecraft.starbox.network.packets.serverbound.play;

import net.defekt.minecraft.starbox.data.DataTypes;
import net.defekt.minecraft.starbox.inventory.ItemStack;
import net.defekt.minecraft.starbox.network.packets.serverbound.ServerboundPacket;

import java.io.DataInputStream;
import java.io.IOException;

public class ClientPlayCreativeInventoryActionPacket extends ServerboundPacket {

    private final short slot;
    private final ItemStack item;

    public ClientPlayCreativeInventoryActionPacket(byte[] data) throws IOException {
        super(data);
        DataInputStream wrapper = getStream();
        slot = wrapper.readShort();
        item = DataTypes.readSlotData(wrapper);
    }

    public short getSlot() {
        return slot;
    }

    public ItemStack getItem() {
        return item;
    }
}
