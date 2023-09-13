package net.defekt.minecraft.starbox.network.packets.clientbound.play;

import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.data.DataTypes;
import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class ServerPlayChatMessagePacket extends ClientboundPacket {

    public ServerPlayChatMessagePacket(ChatComponent message, Position pos, UUID sender) throws IOException {
        super(0x0E);
        if (sender == null) sender = new UUID(0, 0);
        DataOutputStream wrapper = getWrapper();
        DataTypes.writeVarString(wrapper, message.toJson());
        wrapper.writeByte(pos.getId());
        DataTypes.writeUUID(wrapper, sender);
    }

    public enum Position {
        CHAT(0),
        SYSTEM(1),
        ACTION(2);

        private final int id;

        Position(int id) {this.id = id;}

        public int getId() {
            return id;
        }
    }
}
