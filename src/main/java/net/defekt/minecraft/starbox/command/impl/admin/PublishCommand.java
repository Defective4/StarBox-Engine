package net.defekt.minecraft.starbox.command.impl.admin;

import net.defekt.minecraft.starbox.command.Command;
import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.network.PlayerConnection;

public class PublishCommand extends Command {
    public PublishCommand() {
        super("publish", "minecraft.command.publish");
    }

    @Override
    public boolean execute(PlayerConnection player, String command, String[] args) {
        boolean success = player.getServer().publishToLAN();
        player.sendMessage(new ChatComponent.Builder().setTranslate(success ?
                                                                            "commands.publish.success" :
                                                                            "commands.publish.alreadyPublished")
                                                      .setColor(success ? "white" : "red")
                                                      .addWith(ChatComponent.fromString(String.valueOf(player.getServer()
                                                                                                             .getPort())))
                                                      .build());
        return true;
    }
}
