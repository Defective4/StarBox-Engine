package net.defekt.minecraft.starbox.command.impl.admin;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import net.defekt.minecraft.starbox.command.Command;
import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.network.PlayerConnection;

import java.util.Arrays;

public class TellrawCommand extends Command {
    public TellrawCommand() {
        super("tellraw", "minecraft.command.tellraw");
        setArgs("<target> <message>");
    }

    @Override
    public boolean execute(PlayerConnection player, String command, String[] args) {
        if (args.length > 1) {
            PlayerConnection target = player.getServer().getPlayer(args[0]);
            if (target != null) {
                String json = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                try {
                    JsonParser.parseString(json);
                    ChatComponent chat = new Gson().fromJson(json, ChatComponent.class);
                    if (chat != null) target.sendMessage(chat);
                    return true;
                } catch (Exception ignored) {return false;}
            }
        }
        printUsage(player);
        return true;
    }
}
