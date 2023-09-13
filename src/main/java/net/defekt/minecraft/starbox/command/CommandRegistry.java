package net.defekt.minecraft.starbox.command;

import net.defekt.minecraft.starbox.MinecraftServer;
import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.data.Material;
import net.defekt.minecraft.starbox.inventory.ItemStack;
import net.defekt.minecraft.starbox.inventory.PlayerInventory;
import net.defekt.minecraft.starbox.network.Connection;
import net.defekt.minecraft.starbox.network.PlayerConnection;
import net.defekt.minecraft.starbox.world.BlockState;
import net.defekt.minecraft.starbox.world.Location;

import java.util.*;

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
                        Material block = Material.getItemForID(args[6], true);
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
        registerCommand(new Command("give", "minecraft.command.give") {
            @Override
            public boolean execute(PlayerConnection sender, String command, String[] args) {
                if (args.length > 1) {
                    Connection con = sender.getServer().getConnection(args[0]);
                    if (con instanceof PlayerConnection) {
                        PlayerInventory inv = ((PlayerConnection) con).getInventory();
                        Material item = Material.getItemForID(args[1], false);
                        if (item != null) {
                            try {
                                int slot = inv.getFreeSlot();
                                if (slot < 0) {
                                    sender.sendMessage(new ChatComponent.Builder().setColor("red")
                                                                                  .setText(
                                                                                          "No space in target inventory!")
                                                                                  .build());
                                } else {
                                    int count = args.length > 2 ? Integer.parseInt(args[2]) : 1;
                                    if (count > 64) count = 64;
                                    if (count <= 0) count = 1;
                                    ItemStack stack = new ItemStack(item, null, count);
                                    inv.setItem(slot, stack);
                                    sender.sendMessage(new ChatComponent.Builder().setTranslate(
                                                                                          "commands.give.success.single")
                                                                                  .addWith(ChatComponent.fromString(
                                                                                          String.valueOf(count)))
                                                                                  .addWith(ChatComponent.fromString(item.getDisplay())) // TODO
                                                                                  .addWith(ChatComponent.fromString(con.getProfile()
                                                                                                                       .getName()))
                                                                                  .build());
                                }
                                return true;
                            } catch (Exception ignored) {}
                        }
                    }
                }
                printUsage(sender);
                return true;
            }
        }.setArgs("<target> <item> [<count>]"));
        registerCommand(new Command("kick", "minecraft.command.kick") {
            @Override
            public boolean execute(PlayerConnection player, String command, String[] args) {
                if (args.length > 0) {
                    PlayerConnection target = player.getServer().getPlayer(args[0]);
                    if (target != null) {
                        ChatComponent reason = args.length > 1 ?
                                ChatComponent.fromString(String.join(" ", Arrays.copyOfRange(args, 1, args.length))) :
                                new ChatComponent.Builder().setTranslate("multiplayer.disconnect.kicked").build();
                        target.disconnect(reason);
                        player.sendMessage(new ChatComponent.Builder().setTranslate("commands.kick.success")
                                                                      .addWith(ChatComponent.fromString(target.getProfile()
                                                                                                              .getName()))
                                                                      .addWith(reason)
                                                                      .build());
                        return true;
                    }
                }
                printUsage(player);
                return true;
            }
        }.setArgs("<target> [<reason>]"));
        registerCommand(new Command("help") {
            @Override
            public boolean execute(PlayerConnection player, String command, String[] args) {
                return false;
            }
        }.setArgs("[<command>]"));
        registerCommand(new Command("list") {
            @Override
            public boolean execute(PlayerConnection player, String command, String[] args) {
                boolean showUUIDs = args.length > 0 && args[0].equalsIgnoreCase("uuids");
                ChatComponent.Builder profiles = new ChatComponent.Builder();
                profiles.setText("\n");
                Collection<Connection> connections = player.getServer().getOnlineConnections();
                int index = 0;
                for (Connection con : connections) {
                    index++;

                    profiles.addExtra(showUUIDs ?
                                              new ChatComponent.Builder().setText(con.getProfile().getName() + " (")
                                                                         .addExtra(new ChatComponent.Builder().setText(
                                                                                                                      con.getProfile().getUuid().toString())
                                                                                                              .setHoverEvent(
                                                                                                                      ChatComponent.fromString(
                                                                                                                              "Click to copy to clipboard"))
                                                                                                              .setClickEvent(
                                                                                                                      ChatComponent.Builder.ClickEventType.COPY_TO_CLIPBOARD,
                                                                                                                      con.getProfile()
                                                                                                                         .getUuid()
                                                                                                                         .toString())
                                                                                                              .build())
                                                                         .addExtra(ChatComponent.fromString(")"))
                                                                         .build() :
                                              ChatComponent.fromString(con.getProfile().getName()));
                    if (index < connections.size()) profiles.addExtra(ChatComponent.fromString(", \n"));
                }


                int online = connections.size();
                int max = 1;

                player.sendMessage(new ChatComponent.Builder().setTranslate("commands.list.players")
                                                              .addWith(ChatComponent.fromString(String.valueOf(online)))
                                                              .addWith(ChatComponent.fromString(String.valueOf(max)))
                                                              .addWith(profiles.build())
                                                              .build());
                return true;
            }
        }.setArgs("[uuids]"));
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
