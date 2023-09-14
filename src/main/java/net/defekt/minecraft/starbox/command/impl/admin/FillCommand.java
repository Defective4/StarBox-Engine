package net.defekt.minecraft.starbox.command.impl.admin;

import net.defekt.minecraft.starbox.command.Command;
import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.data.Material;
import net.defekt.minecraft.starbox.network.PlayerConnection;
import net.defekt.minecraft.starbox.world.BlockState;
import net.defekt.minecraft.starbox.world.Location;

import java.util.HashMap;
import java.util.Map;

public class FillCommand extends Command {
    public FillCommand() {
        super("fill", "minecraft.command.fill");
        setArgs("<from> <to> <block>");
    }

    @Override
    public boolean execute(PlayerConnection player, String command, String[] args) {
        if (args.length > 6) {
            try {
                int sx = Integer.parseInt(args[0]);
                int sy = Integer.parseInt(args[1]);
                int sz = Integer.parseInt(args[2]);
                int tx = Integer.parseInt(args[3]);
                int ty = Integer.parseInt(args[4]);
                int tz = Integer.parseInt(args[5]);
                Material type = Material.getItemForID(args[6], true);
                Map<Location, BlockState> blocks = new HashMap<>();
                for (int x = sx; x <= tx; x++)
                    for (int y = sy; y <= ty; y++)
                        for (int z = sz; z <= tz; z++)
                            blocks.put(new Location(x, y, z), new BlockState(type));
                player.getWorld().batchFillBlocks(blocks);
                player.sendMessage(new ChatComponent.Builder().setTranslate("commands.fill.success")
                                                              .addWith(ChatComponent.fromString(String.valueOf(blocks.size())))
                                                              .build());

                return true;
            } catch (Exception ignored) {}
        }
        printUsage(player);
        return true;
    }
}
