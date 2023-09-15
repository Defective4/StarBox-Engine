package net.defekt.minecraft.starbox.command.impl.admin;

import net.defekt.minecraft.starbox.command.Command;
import net.defekt.minecraft.starbox.network.PlayerConnection;
import net.defekt.minecraft.starbox.world.Location;

public class TeleportCommand extends Command {

    public TeleportCommand() {
        super("teleport", "minecraft.command.teleport", "tp");
        setArgs("(<destination>|<location>|<targets>)");
    }

    @Override
    public boolean execute(PlayerConnection player, String command, String[] args) {
        switch (args.length) {
            case 4:
            case 3: {
                try {
                    double x = Double.parseDouble(args[args.length - 3]);
                    double y = Double.parseDouble(args[args.length - 2]);
                    double z = Double.parseDouble(args[args.length - 1]);
                    PlayerConnection toTeleport = args.length == 4 ? player.getServer().getPlayer(args[0]) : player;
                    if (toTeleport != null) {
                        toTeleport.teleport(new Location(x, y, z));
                        return true;
                    }
                } catch (Exception ignored) {}
                break;
            }
            case 2:
            case 1: {
                PlayerConnection target = player.getServer().getPlayer(args[args.length - 1]);
                PlayerConnection toTeleport = args.length == 2 ? player.getServer().getPlayer(args[0]) : player;
                if (target != null && toTeleport != null) {
                    toTeleport.teleport(target.getPosition());
                    return true;
                }
                break;
            }
            default:
                break;
        }
        printUsage(player);
        return true;
    }
}
