package net.defekt.minecraft.starbox;

public class Main {
    public static void main(String[] args) {
        try (MinecraftServer srv = new MinecraftServer(null, 25565)) {
            srv.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
