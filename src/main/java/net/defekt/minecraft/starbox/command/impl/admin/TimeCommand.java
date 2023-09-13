package net.defekt.minecraft.starbox.command.impl.admin;

import net.defekt.minecraft.starbox.command.Command;
import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.network.PlayerConnection;
import net.defekt.minecraft.starbox.world.World;

public class TimeCommand extends Command {
    public TimeCommand() {
        super("time", "minecraft.command.time");
        setArgs("(add|query|set)");
    }

    @Override
    public boolean execute(PlayerConnection player, String command, String[] args) {
        if (args.length > 0) {
            String sub = args[0];
            switch (sub.toLowerCase()) {
                case "query": {
                    long time = player.getWorld().getTime();
                    player.sendMessage(new ChatComponent.Builder().setTranslate("commands.time.query")
                                                                  .addWith(ChatComponent.fromString(World.formatTime(
                                                                          time) + " (" + time + " ticks)"))
                                                                  .build());
                }
                case "add":
                case "set": {
                    if (args.length > 1) {
                        try {
                            long time = Long.parseLong(args[1]) + (sub.equalsIgnoreCase("add") ?
                                    player.getWorld().getTime() :
                                    0);
                            player.getWorld().setTime(time);
                            time = player.getWorld().getTime();
                            player.sendMessage(new ChatComponent.Builder().setTranslate("commands.time.set")
                                                                          .addWith(ChatComponent.fromString(World.formatTime(
                                                                                  time) + " (" + time + " ticks)"))
                                                                          .build());
                            return true;
                        } catch (Exception ignored) {}
                    }
                    break;
                }
                default:
                    break;
            }
        }
        printUsage(player);
        return true;
    }
}
