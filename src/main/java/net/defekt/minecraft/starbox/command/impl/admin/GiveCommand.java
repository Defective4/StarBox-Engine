package net.defekt.minecraft.starbox.command.impl.admin;

import net.defekt.minecraft.starbox.command.Command;
import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.data.Material;
import net.defekt.minecraft.starbox.inventory.ItemStack;
import net.defekt.minecraft.starbox.inventory.PlayerInventory;
import net.defekt.minecraft.starbox.network.Connection;
import net.defekt.minecraft.starbox.network.PlayerConnection;

public class GiveCommand extends Command {
    public GiveCommand() {
        super("give", "minecraft.command.give");
        setArgs("<target> <item> [<count>]");
    }

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
                                                                          .setText("No space in target inventory!")
                                                                          .build());
                        } else {
                            int count = args.length > 2 ? Integer.parseInt(args[2]) : 1;
                            if (count > 64) count = 64;
                            if (count <= 0) count = 1;
                            ItemStack stack = new ItemStack(item, null, count);
                            inv.setItem(slot, stack);
                            sender.sendMessage(new ChatComponent.Builder().setTranslate("commands.give.success.single")
                                                                          .addWith(ChatComponent.fromString(String.valueOf(
                                                                                  count)))
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
}
