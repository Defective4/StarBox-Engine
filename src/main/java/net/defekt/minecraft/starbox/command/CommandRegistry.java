package net.defekt.minecraft.starbox.command;

import net.defekt.minecraft.starbox.MinecraftServer;
import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.data.Material;
import net.defekt.minecraft.starbox.network.Connection;
import net.defekt.minecraft.starbox.network.PlayerConnection;
import net.defekt.minecraft.starbox.world.BlockState;
import net.defekt.minecraft.starbox.world.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        registerCommand(new Command("fill", "minecraft.command.fill") {
            @Override
            public boolean execute(PlayerConnection player, String command, String[] args) {
                if (args.length == 7) {
                    try {
                        int sx = Integer.parseInt(args[0]);
                        int sy = Integer.parseInt(args[1]);
                        int sz = Integer.parseInt(args[2]);
                        int tx = Integer.parseInt(args[3]);
                        int ty = Integer.parseInt(args[4]);
                        int tz = Integer.parseInt(args[5]);
                        Material block = Material.getItemForID(args[6]);
                        Map<Location, BlockState> batch = new HashMap<>();
                        for (int x = sx; x <= tx; x++)
                            for (int y = sy; y <= ty; y++)
                                for (int z = sz; z <= tz; z++) {
                                    batch.put(new Location(x, y, z), new BlockState(block, 0));
                                }
                        player.getWorld().batchChangeBlocks(batch);
                        player.sendMessage(new ChatComponent.Builder().setTranslate("commands.fill.success")
                                                                      .addWith(ChatComponent.fromString(String.valueOf(
                                                                              batch.size())))
                                                                      .build());
                        return true;
                    } catch (Exception ignored) {}
                }
                printUsage(player);
                return true;
            }
        }.setArgs("<from> <to> <block>"));
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
