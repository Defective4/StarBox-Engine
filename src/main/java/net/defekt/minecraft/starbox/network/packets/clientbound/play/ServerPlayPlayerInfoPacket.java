package net.defekt.minecraft.starbox.network.packets.clientbound.play;

import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.data.DataTypes;
import net.defekt.minecraft.starbox.data.PlayerProfile;
import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;

import java.io.DataOutputStream;
import java.io.IOException;

public class ServerPlayPlayerInfoPacket extends ClientboundPacket {

    public ServerPlayPlayerInfoPacket(Action action, PlayerProfile... profiles) throws IOException {
        super(0x32);

        DataOutputStream wrapper = getWrapper();
        DataTypes.writeVarInt(wrapper, action.id);
        DataTypes.writeVarInt(wrapper, profiles.length);
        for (PlayerProfile profile : profiles) {
            DataTypes.writeUUID(wrapper, profile.getUuid());
            switch (action) {
                case ADD_PLAYER: {
                    DataTypes.writeVarString(wrapper, profile.getName());
                    wrapper.writeByte(0);
                    DataTypes.writeVarInt(wrapper, profile.getGameMode().getId());
                    DataTypes.writeVarInt(wrapper, (int) profile.getLatency());
                    ChatComponent display = profile.getDisplayName();
                    wrapper.writeBoolean(display != null);
                    if (display != null) {
                        DataTypes.writeVarString(wrapper, display.toJson());
                    }
                    break;
                }
                case REMOVE_PLAYER: {
                    break;
                }
                case UPDATE_DISPLAY_NAME: {
                    ChatComponent display = profile.getDisplayName();
                    wrapper.writeBoolean(display != null);
                    if (display != null) {
                        DataTypes.writeVarString(wrapper, display.toJson());
                    }
                    break;
                }
                case UPDATE_GAMEMODE: {
                    DataTypes.writeVarInt(wrapper, profile.getGameMode().getId());
                    break;
                }
                case UPDATE_LATENCY: {
                    DataTypes.writeVarInt(wrapper, (int) profile.getLatency());
                    break;
                }
            }
        }
    }


    public static enum Action {
        ADD_PLAYER(0),
        UPDATE_GAMEMODE(1),
        UPDATE_LATENCY(2),
        UPDATE_DISPLAY_NAME(3),
        REMOVE_PLAYER(4);

        private final int id;

        private Action(int i) {
            id = i;
        }

        public int getId() {
            return id;
        }
    }
}
