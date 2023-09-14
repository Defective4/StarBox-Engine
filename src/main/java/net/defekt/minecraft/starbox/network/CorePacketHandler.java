package net.defekt.minecraft.starbox.network;

import net.defekt.minecraft.starbox.MinecraftServer;
import net.defekt.minecraft.starbox.command.Command;
import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.data.GameMode;
import net.defekt.minecraft.starbox.data.PlayerProfile;
import net.defekt.minecraft.starbox.inventory.ItemStack;
import net.defekt.minecraft.starbox.network.packets.AnnotatedPacketHandler;
import net.defekt.minecraft.starbox.network.packets.PacketHandlerMethod;
import net.defekt.minecraft.starbox.network.packets.clientbound.login.ServerLoginSuccessPacket;
import net.defekt.minecraft.starbox.network.packets.clientbound.play.ServerPlayJoinGamePacket;
import net.defekt.minecraft.starbox.network.packets.clientbound.play.ServerPlayPlayerPositionAndLookPacket;
import net.defekt.minecraft.starbox.network.packets.clientbound.status.ServerStatusPongPacket;
import net.defekt.minecraft.starbox.network.packets.clientbound.status.ServerStatusResponsePacket;
import net.defekt.minecraft.starbox.network.packets.serverbound.HandshakePacket;
import net.defekt.minecraft.starbox.network.packets.serverbound.login.ClientLoginStartPacket;
import net.defekt.minecraft.starbox.network.packets.serverbound.play.*;
import net.defekt.minecraft.starbox.network.packets.serverbound.status.ClientStatusPingPacket;
import net.defekt.minecraft.starbox.network.packets.serverbound.status.ClientStatusRequestPacket;
import net.defekt.minecraft.starbox.world.Location;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

public class CorePacketHandler extends AnnotatedPacketHandler {
    private final PlayerConnection connection;

    public CorePacketHandler(PlayerConnection connection) {this.connection = connection;}

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

        if (connection.getServer().getConnection(name) != null) {
            connection.disconnect(ChatComponent.fromString("Someone with this name is already playing!"));
            return;
        }

        UUID uid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8));
        PlayerProfile profile = new PlayerProfile(name, uid, GameMode.CREATIVE, null);
        connection.setProfile(profile);


        connection.sendPacket(new ServerLoginSuccessPacket(uid, name));
        connection.setGameState(GameState.PLAY);
        connection.getServer().insertConnection(connection);
        connection.sendPacket(new ServerPlayJoinGamePacket(1,
                                                           false,
                                                           profile.getGameMode(),
                                                           GameMode.NONE,
                                                           "minecraft:overworld",
                                                           connection.getServer().getDimensionCodec(),
                                                           10,
                                                           false,
                                                           true,
                                                           false,
                                                           true));
        connection.sendPacket(new ServerPlayPlayerPositionAndLookPacket(8.5, 16, 8.5, 0f, 0f));
        connection.loadTerrain();
    }

    private void onMovement(Location to) {
        Location from = connection.getPosition();
        connection.setPosition(to);

        int cx = Math.floorDiv(to.getBlockX(), 16);
        int cz = Math.floorDiv(to.getBlockZ(), 16);

        if (cx != Math.floorDiv(from.getBlockX(), 16) || cz != Math.floorDiv(from.getBlockZ(), 16)) {
            connection.loadTerrain();
        }
    }

    @PacketHandlerMethod
    public void onMovement(ClientPlayPositionAndRotationPacket e) {
        onMovement(e.getLocation());
    }

    @PacketHandlerMethod
    public void onMovement(ClientPlayRotationPacket e) {
        Location cur = connection.getPosition();
        onMovement(new Location(cur.getX(), cur.getY(), cur.getZ(), e.getYaw(), e.getPitch()));
    }

    @PacketHandlerMethod
    public void onMovement(ClientPlayPositionPacket e) {
        Location cur = connection.getPosition();
        onMovement(new Location(e.getX(), e.getY(), e.getZ(), cur.getYaw(), cur.getPitch()));
    }

    @PacketHandlerMethod
    public void onMessageReceive(ClientPlayChatMessagePacket packet) {
        String message = packet.getMessage();
        if (message.length() > 256 || message.contains("§")) {
            connection.sendMessage(new ChatComponent.Builder().setTranslate("chat.cannotSend").setColor("red").build());
            return;
        }

        if (message.startsWith("/")) {
            String[] commandData = message.substring(1).split(" ");
            String command = commandData[0].toLowerCase();
            String[] args = Arrays.copyOfRange(commandData, 1, commandData.length);

            Command cmd = connection.getServer().getCommandRegistry().findCommandByAlias(command);
            boolean success;
            if (cmd != null) success = cmd.execute(connection, command, args);
            else success = false;
            if (!success) {
                connection.sendMessage(new ChatComponent.Builder().setColor("red")
                                                                  .setTranslate("command.unknown.command")
                                                                  .build());
                connection.sendMessage(new ChatComponent.Builder().setColor("gray")
                                                                  .setText("/")
                                                                  .setExtra(new ChatComponent[]{
                                                                          new ChatComponent.Builder().setColor("red")
                                                                                                     .setHoverEvent(
                                                                                                             ChatComponent.fromString(
                                                                                                                     "Click to edit the command"))
                                                                                                     .setClickEvent(
                                                                                                             ChatComponent.Builder.ClickEventType.SUGGEST_COMMAND,
                                                                                                             "/" + String.join(
                                                                                                                     " ",
                                                                                                                     commandData))
                                                                                                     .setText(String.join(
                                                                                                             " ",
                                                                                                             commandData)).build(),
                                                                          new ChatComponent.Builder().setColor("red")
                                                                                                     .setTranslate(
                                                                                                             "command.context.here").build()
                                                                  })
                                                                  .build());
            }
            return;
        }

        connection.getServer()
                  .broadcastMessage(new ChatComponent.Builder().setTranslate("chat.type.text")
                                                               .addWith(new ChatComponent.Builder().setText(connection.getProfile()
                                                                                                                      .getName())
                                                                                                   .setHoverEvent(
                                                                                                           ChatComponent.fromString(
                                                                                                                   "Click to send a private message"))
                                                                                                   .setClickEvent(
                                                                                                           ChatComponent.Builder.ClickEventType.SUGGEST_COMMAND,
                                                                                                           "/msg " + connection.getProfile()
                                                                                                                               .getName() + " ")
                                                                                                   .build())
                                                               .addWith(new ChatComponent.Builder().setText(message)
                                                                                                   .build())
                                                               .build());
    }

    @PacketHandlerMethod
    public void onCreativeAction(ClientPlayCreativeInventoryActionPacket packet) {
        short slot = packet.getSlot();
        ItemStack item = packet.getItem();
        connection.getInventory().setItem(slot, item);
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
