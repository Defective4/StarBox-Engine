package net.defekt.minecraft.starbox.command;

import net.defekt.minecraft.starbox.MinecraftServer;
import net.defekt.minecraft.starbox.command.impl.*;
import net.defekt.minecraft.starbox.command.impl.admin.ClearCommand;
import net.defekt.minecraft.starbox.command.impl.admin.FillCommand;
import net.defekt.minecraft.starbox.command.impl.admin.GiveCommand;
import net.defekt.minecraft.starbox.command.impl.admin.KickCommand;

import java.util.*;

public class CommandRegistry {
    private final List<Command> registeredCommands = new ArrayList<>();
    private final MinecraftServer server;

    public CommandRegistry(MinecraftServer server) {
        this.server = server;
        registerSystemCommands();
    }

    private void registerSystemCommands() {
        registerCommand(new ClearCommand());
        registerCommand(new FillCommand());
        registerCommand(new GiveCommand());
        registerCommand(new KickCommand());
        registerCommand(new HelpCommand());
        registerCommand(new ListCommand());
        registerCommand(new MeCommand());
    }

    public void unregisterCommand(Command cmd) {
        registeredCommands.remove(cmd);
    }

    public void registerCommand(Command cmd) {
        if (!registeredCommands.contains(cmd)) registeredCommands.add(cmd);
    }

    public Command getCommand(String name) {
        for (Command cmd : registeredCommands) {
            if (cmd.getName().equalsIgnoreCase(name)) return cmd;
        }
        return null;
    }

    public List<Command> getRegisteredCommands() {
        return new ArrayList<>(registeredCommands);
    }

}
