package net.defekt.minecraft.starbox.network.packets.clientbound.play;

import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.tags.collection.CompoundTag;
import net.defekt.minecraft.starbox.data.DataTypes;
import net.defekt.minecraft.starbox.data.GameMode;
import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;

import java.io.DataOutputStream;
import java.io.IOException;

public class ServerPlayJoinGamePacket extends ClientboundPacket {
    public ServerPlayJoinGamePacket(int entityID, boolean hardcore, GameMode gameMode, GameMode previousGameMode, String worldName, CompoundTag dimCodec, int renderDistance, boolean reducedDebugInfo, boolean respawnScreen, boolean debug, boolean flat) throws IOException {
        super(0x24);

        DataOutputStream wrapper = getWrapper();
        wrapper.writeInt(entityID);
        wrapper.writeBoolean(hardcore);
        wrapper.writeByte(gameMode.getId());
        wrapper.writeByte(previousGameMode.getId());
        DataTypes.writeVarInt(wrapper, 1);
        DataTypes.writeVarString(wrapper, worldName);
        Nbt nbt = new Nbt();
        nbt.toStream(dimCodec, wrapper);
        nbt.toStream(((CompoundTag) dimCodec.getCompound("minecraft:dimension_type")
                                            .getList("value")
                                            .get(0)).getCompound("element"), wrapper);
        DataTypes.writeVarString(wrapper, worldName);
        wrapper.writeLong(0);
        wrapper.writeByte(0);
        DataTypes.writeVarInt(wrapper, renderDistance);
        wrapper.writeBoolean(reducedDebugInfo);
        wrapper.writeBoolean(respawnScreen);
        wrapper.writeBoolean(debug);
        wrapper.writeBoolean(flat);
    }
}
