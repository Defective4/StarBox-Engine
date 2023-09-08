package net.defekt.minecraft.starbox.network.packets.clientbound.play;

import net.defekt.minecraft.starbox.data.DataTypes;
import net.defekt.minecraft.starbox.inventory.ItemStack;
import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;

import java.io.DataOutputStream;
import java.io.IOException;

public class ServerPlaySetSlotPacket extends ClientboundPacket {
    public ServerPlaySetSlotPacket(int window, int slot, ItemStack item) throws IOException {
        super(0x15);
        DataOutputStream wrapper = getWrapper();
        wrapper.writeByte(window);
        wrapper.writeShort(slot);
        DataTypes.writeSlotData(wrapper, item);
    }
}
