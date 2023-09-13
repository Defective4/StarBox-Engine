package net.defekt.minecraft.starbox.sound;

public enum SoundCategory {
    MASTER(0),
    MUSIC(1),
    RECORD(2),
    WEATHER(3),
    BLOCKS(4),
    HOSTILE(5),
    FRIENDLY(6),
    PLAYERS(7),
    AMBIEND(8),
    VOICE(9);

    private final int id;

    SoundCategory(int id) {this.id = id;}

    public int getId() {
        return id;
    }
}
