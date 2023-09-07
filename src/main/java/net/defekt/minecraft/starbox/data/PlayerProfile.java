package net.defekt.minecraft.starbox.data;

import java.util.UUID;

public class PlayerProfile {
    private final String name;
    private final UUID uuid;
    private GameMode gameMode;
    private long latency = 0;
    private ChatComponent displayName;

    public PlayerProfile(String name, UUID uuid, GameMode gameMode, ChatComponent displayName) {
        this.name = name;
        this.uuid = uuid;
        this.gameMode = gameMode;
        this.displayName = displayName;
    }

    public UUID getUuid() {
        return uuid;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public long getLatency() {
        return latency;
    }

    public void setLatency(long latency) {
        this.latency = latency;
    }

    public ChatComponent getDisplayName() {
        return displayName;
    }

    public void setDisplayName(ChatComponent displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }
}
