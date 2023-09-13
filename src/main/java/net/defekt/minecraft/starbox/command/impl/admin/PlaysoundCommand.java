package net.defekt.minecraft.starbox.command.impl.admin;

import net.defekt.minecraft.starbox.command.Command;
import net.defekt.minecraft.starbox.network.Connection;
import net.defekt.minecraft.starbox.network.PlayerConnection;
import net.defekt.minecraft.starbox.sound.SoundCategory;
import net.defekt.minecraft.starbox.world.Location;

public class PlaysoundCommand extends Command {
    public PlaysoundCommand() {
        super("playsound", "minecraft.command.playsound");
        setArgs("<sound> (ambient|block|hostile|master|music|neutral|player|record|voice|weather) <pos> [<volume>] [<pitch>] [<target>]");
    }

    @Override
    public boolean execute(PlayerConnection player, String command, String[] args) {
        if (args.length > 4) {
            String sound = args[0];
            SoundCategory type = SoundCategory.getOrDefault(args[1]);
            try {
                int x = Integer.parseInt(args[2]);
                int y = Integer.parseInt(args[3]);
                int z = Integer.parseInt(args[4]);
                float vol = args.length > 5 ? Float.parseFloat(args[5]) : 1f;
                float pitch = args.length > 6 ? Float.parseFloat(args[6]) : 1f;

                PlayerConnection target;
                if (args.length > 7) {
                    target = player.getServer().getPlayer(args[7]);
                    if (target == null) {
                        printUsage(player);
                        return true;
                    }
                } else target = null;

                if (target == null) for (Connection con : player.getServer().getOnlineConnections())
                    con.playSound(sound, type, new Location(x, y, z), vol, pitch);
                else target.playSound(sound, type, new Location(x, y, z), vol, pitch);
                return true;
            } catch (Exception ignored) {}
        }
        printUsage(player);
        return true;
    }
}
