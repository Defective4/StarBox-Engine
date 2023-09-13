package net.defekt.minecraft.starbox.command;

import net.defekt.minecraft.starbox.MinecraftServer;
import net.defekt.minecraft.starbox.command.impl.HelpCommand;
import net.defekt.minecraft.starbox.command.impl.ListCommand;
import net.defekt.minecraft.starbox.command.impl.MeCommand;
import net.defekt.minecraft.starbox.command.impl.MsgCommand;
import net.defekt.minecraft.starbox.command.impl.admin.*;

import java.util.ArrayList;
import java.util.List;

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
        registerCommand(new MsgCommand());
        registerCommand(new PublishCommand());
        registerCommand(new SayCommand());
        registerCommand(new SetblockCommand());
        registerCommand(new TellrawCommand());
        registerCommand(new PlaysoundCommand());
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

    public Command findCommandByAlias(String name) {
        for (Command cmd : registeredCommands) {
            if (cmd.getName().equalsIgnoreCase(name)) return cmd;
            else for (String alias : cmd.getAliases())
                if (name.equalsIgnoreCase(alias)) return cmd;
        }
        return null;
    }

    public List<Command> getRegisteredCommands() {
        return new ArrayList<>(registeredCommands);
    }

}
