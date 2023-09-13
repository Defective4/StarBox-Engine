package net.defekt.minecraft.starbox.command.impl.admin;

import net.defekt.minecraft.starbox.command.Command;
import net.defekt.minecraft.starbox.network.PlayerConnection;

public class PublishCommand extends Command {
    public PublishCommand() {
        super("publish", "minecraft.command.publish");
    }

    @Override
    public boolean execute(PlayerConnection player, String command, String[] args) {
        // TODO
        return false;
    }
}
