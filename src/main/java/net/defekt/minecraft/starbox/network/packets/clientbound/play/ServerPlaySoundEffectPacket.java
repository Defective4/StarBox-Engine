package net.defekt.minecraft.starbox.network.packets.clientbound.play;

import net.defekt.minecraft.starbox.data.DataTypes;
import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;
import net.defekt.minecraft.starbox.sound.Sound;
import net.defekt.minecraft.starbox.sound.SoundCategory;

import java.io.DataOutputStream;
import java.io.IOException;

public class ServerPlaySoundEffectPacket extends ClientboundPacket {
    public ServerPlaySoundEffectPacket(Sound snd, SoundCategory cat, int x, int y, int z, float vol, float pitch) throws IOException {
        super(0x51);
        DataOutputStream wrapper = getWrapper();
        DataTypes.writeVarInt(wrapper, snd.getId());
        DataTypes.writeVarInt(wrapper, cat.getId());
        wrapper.writeInt(x * 8);
        wrapper.writeInt(y * 8);
        wrapper.writeInt(z * 8);
        wrapper.writeFloat(vol);
        wrapper.writeFloat(pitch);
    }
}
