package net.defekt.minecraft.starbox.command.impl;

import net.defekt.minecraft.starbox.command.Command;
import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.network.PlayerConnection;

public class MeCommand extends Command {
    public MeCommand() {
        super("me", "minecraft.command.me");
        setArgs("<action>");
    }

    @Override
    public boolean execute(PlayerConnection player, String command, String[] args) {
        if (args.length > 0) {
            player.getServer()
                  .broadcastMessage(new ChatComponent.Builder().setTranslate("chat.type.emote")
                                                               .addWith(ChatComponent.fromString(player.getProfile()
                                                                                                       .getName()))
                                                               .addWith(new ChatComponent.Builder().setText(String.join(
                                                                       " ",
                                                                       args)).build())
                                                               .build());
            return true;
        }
        printUsage(player);
        return true;
    }
}
