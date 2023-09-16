package net.defekt.minecraft.starbox.network;

import net.defekt.minecraft.starbox.MinecraftServer;
import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.data.PlayerProfile;
import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;
import net.defekt.minecraft.starbox.network.packets.clientbound.play.ServerPlayChatMessagePacket;
import net.defekt.minecraft.starbox.network.packets.clientbound.play.ServerPlayNamedSoundEffectPacket;
import net.defekt.minecraft.starbox.network.packets.clientbound.play.ServerPlaySoundEffectPacket;
import net.defekt.minecraft.starbox.sound.Sound;
import net.defekt.minecraft.starbox.sound.SoundCategory;
import net.defekt.minecraft.starbox.storage.Operators;
import net.defekt.minecraft.starbox.world.Location;

import java.io.IOException;

public abstract class Connection {
    private final MinecraftServer server;
    private String announcedHost = null;
    private int announcedPort = -1;
    private int protocol = -1;

    private PlayerProfile profile = null;

    public Connection(MinecraftServer server) {this.server = server;}

    public PlayerProfile getProfile() {
        return profile;
    }

    protected void setProfile(PlayerProfile profile) {
        this.profile = profile;
    }

    public int getProtocol() {
        return protocol;
    }

    protected void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public String getAnnouncedHost() {
        return announcedHost;
    }

    protected void setAnnouncedHost(String announcedHost) {
        this.announcedHost = announcedHost;
    }

    public abstract void sendPacket(ClientboundPacket packet);

    public boolean isOperator() {
        if (profile == null) return false;
        return getServer().getOperators().isOp(getProfile().getName());
    }

    public boolean setOperator(boolean op) {
        if(profile == null) return false;
        Operators ops = getServer().getOperators();
        if(op)
            return ops.op(profile.getName());
        else
            return ops.deop(profile.getName());
    }

    public void sendMessage(ChatComponent message) {
        try {
            sendPacket(new ServerPlayChatMessagePacket(message, ServerPlayChatMessagePacket.Position.CHAT, null));
        } catch (IOException ignored) {}
    }

    public void playSound(Sound sound, SoundCategory category, Location loc, float volume, float pitch) {
        try {
            sendPacket(new ServerPlaySoundEffectPacket(sound,
                                                       category,
                                                       loc.getBlockX(),
                                                       loc.getBlockY(),
                                                       loc.getBlockZ(),
                                                       volume,
                                                       pitch));
        } catch (IOException ignored) {}
    }

    public void playSound(String sound, SoundCategory category, Location loc, float volume, float pitch) {
        try {
            sendPacket(new ServerPlayNamedSoundEffectPacket(sound,
                                                            category,
                                                            loc.getBlockX(),
                                                            loc.getBlockY(),
                                                            loc.getBlockZ(),
                                                            volume,
                                                            pitch));
        } catch (IOException ignored) {}
    }

    public int getAnnouncedPort() {
        return announcedPort;
    }

    protected void setAnnouncedPort(int announcedPort) {
        this.announcedPort = announcedPort;
    }

    public MinecraftServer getServer() {
        return server;
    }

    public abstract void handle() throws Exception;
}
