package net.defekt.minecraft.starbox.command.impl.admin;

import net.defekt.minecraft.starbox.command.Command;
import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.network.PlayerConnection;
import net.defekt.minecraft.starbox.storage.Whitelist;

import java.util.List;

public class WhitelistCommand extends Command {
    public WhitelistCommand() {
        super("whitelist", "minecraft.command.whitelist");
        setArgs("(add|list|off|on|reload|remove)");
    }

    @Override
    public boolean execute(PlayerConnection player, String command, String[] args) {
        if (args.length > 0) {
            Whitelist whitelist = player.getServer().getWhitelist();
            switch (args[0].toLowerCase()) {
                case "remove": {
                    if (args.length > 1) {
                        ChatComponent.Builder bd = new ChatComponent.Builder();
                        if (whitelist.remove(args[1])) bd.setTranslate("commands.whitelist.remove.success")
                                                         .addWith(ChatComponent.fromString(args[1]));
                        else bd.setTranslate("commands.whitelist.remove.failed").setColor("red");
                        player.sendMessage(bd.build());
                        return true;
                    }
                    break;
                }
                case "off":
                case "on": {
                    boolean on = args[0].equalsIgnoreCase("on");
                    whitelist.setEnabled(on);
                    player.sendMessage(new ChatComponent.Builder().setTranslate("commands.whitelist." + (on ?
                            "enabled" :
                            "disabled")).build());
                    return true;
                }
                case "list": {
                    List<String> whitelisted = whitelist.getWhitelisted();
                    player.sendMessage((whitelisted.size() > 0 ?
                            new ChatComponent.Builder().setTranslate("commands.whitelist.list")
                                                       .addWith(ChatComponent.fromString(String.valueOf(whitelisted.size())))
                                                       .addWith(ChatComponent.fromString("\n" + String.join(",\n",
                                                                                                            whitelisted))) :
                            new ChatComponent.Builder().setTranslate("commands.whitelist.none")).build());
                    return true;
                }
                case "add": {
                    if (args.length > 1) {
                        ChatComponent.Builder bd = new ChatComponent.Builder();
                        if (whitelist.add(args[1])) bd.setTranslate("commands.whitelist.add.success")
                                                      .addWith(ChatComponent.fromString(args[1]));
                        else bd.setTranslate("commands.whitelist.add.failed").setColor("red");
                        player.sendMessage(bd.build());
                        return true;
                    }
                    break;
                }
                case "reload": { // TODO
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
