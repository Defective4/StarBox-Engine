package net.defekt.minecraft.starbox.command;

import net.defekt.minecraft.starbox.MinecraftServer;
import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.network.Connection;
import net.defekt.minecraft.starbox.network.PlayerConnection;

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
        registerCommand(new Command("clear", "minecraft.command.clear") {
            @Override
            public boolean execute(PlayerConnection player, String command, String[] args) {
                Connection target = args.length == 0 ? player : server.getConnection(args[0]);
                if (target instanceof PlayerConnection) {
                    int cleared = ((PlayerConnection) target).getInventory().clear();
                    target.sendMessage(new ChatComponent.Builder().setTranslate("commands.clear.success.single")
                                                                  .addWith(new ChatComponent.Builder().setText(Integer.toString(
                                                                          cleared)).build())
                                                                  .addWith(new ChatComponent.Builder().setText(target.getProfile()
                                                                                                                     .getName())
                                                                                                      .build())
                                                                  .build());
                } else {
                    printUsage(player);
                }
                return true;
            }
        }.setArgs("[<target>]"));
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
