package net.defekt.minecraft.starbox.network;

import net.defekt.minecraft.starbox.MinecraftServer;
import net.defekt.minecraft.starbox.network.packets.clientbound.ClientboundPacket;

import java.io.IOException;
import java.util.UUID;

public abstract class Connection {
    private final MinecraftServer server;

    public Connection(MinecraftServer server) {this.server = server;}

    private String announcedHost = null;
    private int announcedPort = -1;
    private int protocol = -1;

    private String name = null;
    private UUID uuid = null;

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    protected void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    public abstract void sendPacket(ClientboundPacket packet) throws IOException;

    protected void setAnnouncedHost(String announcedHost) {
        this.announcedHost = announcedHost;
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
