package net.defekt.minecraft.starbox.data;

public enum GameMode {
    NONE(-1),
    SURVIVAL(0),
    CREATIVE(1),
    ADVENTURE(2),
    SPECTATOR(3);

    private final int id;

    GameMode(int id) {this.id = id;}

    public int getId() {
        return id;
    }
}
