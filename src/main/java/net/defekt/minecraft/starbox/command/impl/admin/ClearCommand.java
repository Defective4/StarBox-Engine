package net.defekt.minecraft.starbox.command.impl.admin;

import net.defekt.minecraft.starbox.command.Command;
import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.network.Connection;
import net.defekt.minecraft.starbox.network.PlayerConnection;

public class ClearCommand extends Command {
    public ClearCommand() {
        super("clear", "minecraft.command.clear");
        setArgs("[<target>]");
    }

    @Override
    public boolean execute(PlayerConnection player, String command, String[] args) {
        Connection target = args.length == 0 ? player : player.getServer().getConnection(args[0]);
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
}
