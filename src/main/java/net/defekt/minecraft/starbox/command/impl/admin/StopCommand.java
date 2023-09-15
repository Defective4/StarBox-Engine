package net.defekt.minecraft.starbox.command.impl.admin;

import net.defekt.minecraft.starbox.command.Command;
import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.network.PlayerConnection;

public class StopCommand extends Command {
    public StopCommand() {
        super("stop", "minecraft.command.stop");
    }

    @Override
    public boolean execute(PlayerConnection player, String command, String[] args) {
        player.sendMessage(new ChatComponent.Builder()
                                   .setTranslate("commands.stop.stopping")
                                   .build());
        System.exit(1);
        return true;
    }
}
