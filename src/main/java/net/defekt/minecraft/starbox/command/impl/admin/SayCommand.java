package net.defekt.minecraft.starbox.command.impl.admin;

import net.defekt.minecraft.starbox.command.Command;
import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.network.PlayerConnection;

public class SayCommand extends Command {
    public SayCommand() {
        super("say", "minecraft.command.say");
        setArgs("<message>");
    }

    @Override
    public boolean execute(PlayerConnection player, String command, String[] args) {
        if (args.length > 0) {
            player.getServer()
                  .broadcastMessage(new ChatComponent.Builder().setTranslate("chat.type.announcement")
                                                               .addWith(new ChatComponent.Builder().setHoverEvent(
                                                                                                           ChatComponent.fromString(
                                                                                                                   "Click to send a private message"))
                                                                                                   .setClickEvent(
                                                                                                           ChatComponent.Builder.ClickEventType.SUGGEST_COMMAND,
                                                                                                           "/msg " + player.getProfile()
                                                                                                                           .getName() + " ")
                                                                                                   .setText(player.getProfile()
                                                                                                                  .getName())
                                                                                                   .build())
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
