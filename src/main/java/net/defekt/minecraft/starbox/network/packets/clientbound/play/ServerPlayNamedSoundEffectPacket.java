package net.defekt.minecraft.starbox.network.packets.clientbound.play;

import net.defekt.minecraft.starbox.data.DataTypes;
import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;
import net.defekt.minecraft.starbox.sound.SoundCategory;

import java.io.DataOutputStream;
import java.io.IOException;

public class ServerPlayNamedSoundEffectPacket extends ClientboundPacket {
    public ServerPlayNamedSoundEffectPacket(String sound, SoundCategory category, int x, int y, int z, float volume, float pitch) throws IOException {
        super(0x18);
        DataOutputStream wrapper = getWrapper();
        DataTypes.writeVarString(wrapper, sound);
        DataTypes.writeVarInt(wrapper, category.getId());
        wrapper.writeInt(x * 8);
        wrapper.writeInt(y * 8);
        wrapper.writeInt(z * 8);
        wrapper.writeFloat(volume);
        wrapper.writeFloat(pitch);
    }
}
