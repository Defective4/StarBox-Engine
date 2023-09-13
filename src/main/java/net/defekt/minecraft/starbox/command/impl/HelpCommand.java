package net.defekt.minecraft.starbox.command.impl;

import net.defekt.minecraft.starbox.command.Command;
import net.defekt.minecraft.starbox.network.PlayerConnection;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help");
        setArgs("[<command>]");
    }

    @Override
    public boolean execute(PlayerConnection player, String command, String[] args) {
        return false;
    }
}
