package net.defekt.minecraft.starbox.network;

import net.defekt.minecraft.starbox.MinecraftServer;
import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.data.GameMode;
import net.defekt.minecraft.starbox.network.packets.AnnotatedPacketHandler;
import net.defekt.minecraft.starbox.network.packets.PacketHandlerMethod;
import net.defekt.minecraft.starbox.network.packets.clientbound.login.ServerLoginSuccessPacket;
import net.defekt.minecraft.starbox.network.packets.clientbound.play.ServerPlayJoinGamePacket;
import net.defekt.minecraft.starbox.network.packets.clientbound.play.ServerPlayPlayerPositionAndLookPacket;
import net.defekt.minecraft.starbox.network.packets.clientbound.status.ServerStatusPongPacket;
import net.defekt.minecraft.starbox.network.packets.clientbound.status.ServerStatusResponsePacket;
import net.defekt.minecraft.starbox.network.packets.serverbound.HandshakePacket;
import net.defekt.minecraft.starbox.network.packets.serverbound.login.ClientLoginStartPacket;
import net.defekt.minecraft.starbox.network.packets.serverbound.status.ClientStatusPingPacket;
import net.defekt.minecraft.starbox.network.packets.serverbound.status.ClientStatusRequestPacket;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class CorePacketHandler extends AnnotatedPacketHandler {
    private final PlayerConnection connection;

    public CorePacketHandler(PlayerConnection connection) throws IOException {
        this.connection = connection;
    }

    @PacketHandlerMethod
    public void onLoginStart(ClientLoginStartPacket packet) throws Exception {
        int proto = connection.getProtocol();
        if (proto != MinecraftServer.PROTOCOL) {
            connection.disconnect(new ChatComponent.Builder().setTranslate("multiplayer.disconnect.outdated_" + (proto < MinecraftServer.PROTOCOL ?
                    "client" :
                    "server")).addWith(new ChatComponent.Builder().setText("1.16.5").build()).build());
            return;
        }

        String name = packet.getName();
        int len = name.length();
        if (len < 3 || len > 16) {
            connection.disconnect(ChatComponent.fromString("Your username must be between 3 and 16 characters long!"));
            return;
        }

        for (char c : name.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                connection.disconnect(ChatComponent.fromString("Detected invalid characters in your username!"));
                return;
            }
        }

        UUID uid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8));
        connection.setName(name);
        connection.setUuid(uid);


        connection.sendPacket(new ServerLoginSuccessPacket(uid, name));
        connection.setGameState(GameState.PLAY);
        connection.sendPacket(new ServerPlayJoinGamePacket(1,
                                                           false,
                                                           GameMode.CREATIVE,
                                                           GameMode.NONE,
                                                           "minecraft:overworld",
                                                           connection.getServer().getDimensionCodec(),
                                                           4,
                                                           false,
                                                           true,
                                                           false,
                                                           false));
        connection.sendPacket(new ServerPlayPlayerPositionAndLookPacket(8.5, 8.5, 8.5, 0f, 0f));
    }

    @PacketHandlerMethod
    public void onStatusRequest(ClientStatusRequestPacket packet) throws IOException {
        connection.sendPacket(new ServerStatusResponsePacket(connection.getServer().getCurrentStatus()));
    }

    @PacketHandlerMethod
    public void onStatusPong(ClientStatusPingPacket packet) throws IOException {
        connection.sendPacket(new ServerStatusPongPacket(packet.getPayload()));
    }

    @PacketHandlerMethod
    public void onHandshake(HandshakePacket packet) {
        int protocol = packet.getProtocol();
        String host = packet.getHost();
        int port = packet.getPort();
        int state = packet.getState();

        connection.setAnnouncedHost(host);
        connection.setAnnouncedPort(port);
        connection.setProtocol(protocol);

        connection.setGameState(state == 1 ? GameState.STATUS : GameState.LOGIN);
    }
}
