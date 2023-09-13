package net.defekt.minecraft.starbox.command;

import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.network.PlayerConnection;

import java.util.Objects;

public abstract class Command {
    private final String name;
    private final String[] aliases;
    private final String permission;

    private String description = null;
    private String args = null;

    public Command(String name, String permission, String... aliases) {
        Objects.requireNonNull(name);
        this.name = name.toLowerCase();
        this.aliases = aliases;
        this.permission = permission;
    }

    public Command(String name) {
        this.name = name;
        this.aliases = new String[0];
        this.permission = null;
    }

    public String getDescription() {
        return description;
    }

    public Command setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getArgs() {
        return args;
    }

    public Command setArgs(String args) {
        this.args = args;
        return this;
    }

    public String getPermission() {
        return permission;
    }

    public abstract boolean execute(PlayerConnection player, String command, String[] args);

    public String getName() {
        return name;
    }

    public String[] getAliases() {
        return aliases;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return Objects.equals(name, command.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public void printUsage(PlayerConnection connection) {
        connection.sendMessage(new ChatComponent.Builder()
                                       .setColor("red")
                                       .setText(getUsage())
                                       .build());
    }

    public String getUsage() {
        return "/" + name + " " + (args == null ? "" : args);
    }
}
