package net.defekt.minecraft.starbox.command.impl.admin;

import net.defekt.minecraft.starbox.command.Command;
import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.network.PlayerConnection;

import java.util.Arrays;

public class KickCommand extends Command {
    public KickCommand() {
        super("kick", "minecraft.command.kick");
        setArgs("<target> [<reason>]");
    }

    @Override
    public boolean execute(PlayerConnection player, String command, String[] args) {
        if (args.length > 0) {
            PlayerConnection target = player.getServer().getPlayer(args[0]);
            if (target != null) {
                ChatComponent reason = args.length > 1 ?
                        ChatComponent.fromString(String.join(" ", Arrays.copyOfRange(args, 1, args.length))) :
                        new ChatComponent.Builder().setTranslate("multiplayer.disconnect.kicked").build();
                target.disconnect(reason);
                player.sendMessage(new ChatComponent.Builder().setTranslate("commands.kick.success")
                                                              .addWith(ChatComponent.fromString(target.getProfile()
                                                                                                      .getName()))
                                                              .addWith(reason)
                                                              .build());
                return true;
            }
        }
        printUsage(player);
        return true;
    }
}
