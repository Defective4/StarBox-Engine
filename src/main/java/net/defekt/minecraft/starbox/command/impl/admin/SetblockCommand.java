package net.defekt.minecraft.starbox.command.impl.admin;

import net.defekt.minecraft.starbox.command.Command;
import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.data.Material;
import net.defekt.minecraft.starbox.network.PlayerConnection;
import net.defekt.minecraft.starbox.world.BlockState;

public class SetblockCommand extends Command {
    public SetblockCommand() {
        super("setblock", "minecraft.command.setblock");
        setArgs("<pos> <block>");
    }

    @Override
    public boolean execute(PlayerConnection player, String command, String[] args) {
        if (args.length > 3) {
            try {
                int x = Integer.parseInt(args[0]);
                int y = Integer.parseInt(args[1]);
                int z = Integer.parseInt(args[2]);
                Material block = Material.getItemForID(args[3], true);
                player.getWorld().setBlockAt(x, y, z, new BlockState(block, 0));
                player.sendMessage(new ChatComponent.Builder()
                                           .setTranslate("commands.setblock.success")
                                           .addWith(ChatComponent.fromString(String.valueOf(x)))
                                           .addWith(ChatComponent.fromString(String.valueOf(y)))
                                           .addWith(ChatComponent.fromString(String.valueOf(z)))
                                           .build());
                return true;
            } catch (Exception ignored) {}
        }
        printUsage(player);
        return true;
    }
}
