package net.defekt.minecraft.starbox.network.packets.serverbound;

import net.defekt.minecraft.starbox.Cancellable;
import net.defekt.minecraft.starbox.network.GameState;
import net.defekt.minecraft.starbox.network.packets.serverbound.login.ClientLoginStartPacket;
import net.defekt.minecraft.starbox.network.packets.serverbound.status.ClientStatusPingPacket;
import net.defekt.minecraft.starbox.network.packets.serverbound.status.ClientStatusRequestPacket;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ServerboundPacket implements Cancellable {
    private final byte[] data;
    private boolean cancelled = false;

    protected ServerboundPacket(byte[] data) {this.data = data;}

    public byte[] getData() {
        return data;
    }

    public DataInputStream getStream() {
        return new DataInputStream(new ByteArrayInputStream(data));
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public static class Registry {

        private static final Map<GameState, Map<Integer, Class<? extends ServerboundPacket>>> PACKETS = new HashMap<>();

        static {
            PACKETS.put(GameState.HANDSHAKING, new HashMap<Integer, Class<? extends ServerboundPacket>>() {
                {
                    put(0x00, HandshakePacket.class);
                }
            });
            PACKETS.put(GameState.STATUS, new HashMap<Integer, Class<? extends ServerboundPacket>>() {
                {
                    put(0x00, ClientStatusRequestPacket.class);
                    put(0x01, ClientStatusPingPacket.class);
                }
            });
            PACKETS.put(GameState.LOGIN, new HashMap<Integer, Class<? extends ServerboundPacket>>() {
                {
                    put(0x00, ClientLoginStartPacket.class);
                }
            });
            PACKETS.put(GameState.PLAY, new HashMap<Integer, Class<? extends ServerboundPacket>>() {
                {

                }
            });
        }

        public static Class<? extends ServerboundPacket> getPacketForID(GameState state, int id) {
            return PACKETS.getOrDefault(state, Collections.emptyMap()).get(id);
        }
    }
}
