package net.defekt.minecraft.starbox.command.impl;

import net.defekt.minecraft.starbox.command.Command;
import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.network.PlayerConnection;

import java.util.Arrays;

public class MsgCommand extends Command {

    public MsgCommand() {
        super("msg", "minecraft.command.msg", "tell", "w");
        setArgs("<target> <message>");
    }

    @Override
    public boolean execute(PlayerConnection player, String command, String[] args) {
        if(args.length>1) {
            PlayerConnection target = player.getServer().getPlayer(args[0]);
            if(target!=null) {
                String msg = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                player.sendMessage(new ChatComponent.Builder()
                                           .setTranslate("commands.message.display.outgoing")
                                           .setColor("gray")
                                           .setWith(new ChatComponent[] {
                                                   ChatComponent.fromString(target.getProfile().getName()),
                                                   new ChatComponent.Builder().setText(msg).build()
                                           })
                                           .build());
                target.sendMessage(new ChatComponent.Builder()
                                           .setTranslate("commands.message.display.incoming")
                                           .setColor("gray")
                                           .setWith(new ChatComponent[] {
                                                   ChatComponent.fromString(player.getProfile().getName()),
                                                   new ChatComponent.Builder().setText(msg).build()
                                           }).build());
                return true;
            }
        }
        printUsage(player);
        return true;
    }
}
