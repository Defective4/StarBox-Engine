package net.defekt.minecraft.starbox.command.impl.admin;

import net.defekt.minecraft.starbox.command.Command;
import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.network.PlayerConnection;

public class DeopCommand extends Command {
    public DeopCommand() {
        super("deop", "minecraft.command.deop");
        setArgs("<target>");
    }

    @Override
    public boolean execute(PlayerConnection player, String command, String[] args) {
        if (args.length > 0) {
            PlayerConnection target = player.getServer().getPlayer(args[0]);
            if (target != null) {
                player.sendMessage(
                        (target.setOperator(false) ? new ChatComponent.Builder()
                                .setTranslate("commands.deop.success")
                                .addWith(ChatComponent.fromString(target.getProfile().getName())): new ChatComponent.Builder()
                                .setTranslate("commands.deop.failed")).build()
                );
                return true;
            }
        }
        printUsage(player);
        return true;
    }
}
