package net.defekt.minecraft.starbox.network.packets.clientbound.play;

import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;

import java.io.DataOutputStream;
import java.io.IOException;

public class ServerPlayGameStatePacket extends ClientboundPacket {

    public ServerPlayGameStatePacket(Reason reason, float value) throws IOException {
        super(0x1D);
        DataOutputStream wrapper = getWrapper();
        wrapper.writeByte(reason.id);
        wrapper.writeFloat(value);
    }

    public enum Reason {
        NO_RESPAWN_BLOCK(0),
        END_RAIN(1),
        BEGIN_RAIN(2),
        CHANGE_GAMEMODE(3),
        WIN_GAME(4),
        DEMO_SCREEN(5),
        ARROW_HIT_PLAYER(6),
        RAIN_LEVEL_CHANGE(7),
        THUNDER_LEVEL_CHANGE(8),
        PUFFERFISH_STING(9),
        ELDER_GUARDIAN(10),
        CHANGE_RESPAWN_SCREEN(11);

        private final int id;

        Reason(int id) {
            this.id = id;
        }
    }
}
