package net.defekt.minecraft.starbox.command.impl.admin;

import net.defekt.minecraft.starbox.command.Command;
import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.network.PlayerConnection;

public class TimeCommand extends Command {
    public TimeCommand() {
        super("time", "minecraft.command.time");
        setArgs("(add|query|set)");
    }

    @Override
    public boolean execute(PlayerConnection player, String command, String[] args) {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "query": {
                    player.sendMessage(new ChatComponent.Builder().setTranslate("commands.time.query")
                                                                  .addWith(ChatComponent.fromString(player.getWorld()
                                                                                                          .getParsedTime()))
                                                                  .build());
                    return true;
                }
                case "add":
                case "set": {
                    if (args.length > 1) {
                        try {
                            int time = Integer.parseInt(args[1]) + (args[0].equalsIgnoreCase("add") ?
                                    player.getWorld().getTime() :
                                    0);
                            player.getWorld().setTime(time);
                            player.sendMessage(new ChatComponent.Builder().setTranslate("commands.time.set")
                                                                          .addWith(ChatComponent.fromString(player.getWorld()
                                                                                                                  .getParsedTime()))
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
