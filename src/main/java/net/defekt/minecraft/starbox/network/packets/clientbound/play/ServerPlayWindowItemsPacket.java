package net.defekt.minecraft.starbox.network.packets.clientbound.play;

import net.defekt.minecraft.starbox.data.DataTypes;
import net.defekt.minecraft.starbox.inventory.ItemStack;
import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;

import java.io.DataOutputStream;
import java.io.IOException;

public class ServerPlayWindowItemsPacket extends ClientboundPacket {

    public ServerPlayWindowItemsPacket(int window, ItemStack... items) throws IOException {
        super(0x13);
        DataOutputStream wrapper = getWrapper();
        wrapper.writeByte(window);
        wrapper.writeShort(items.length);
        for (ItemStack item : items)
            DataTypes.writeSlotData(wrapper, item);
    }
}
