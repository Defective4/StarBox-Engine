package net.defekt.minecraft.starbox.command.impl;

import net.defekt.minecraft.starbox.command.Command;
import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.network.Connection;
import net.defekt.minecraft.starbox.network.PlayerConnection;

import java.util.Collection;

public class ListCommand extends Command {
    public ListCommand() {
        super("list", "minecraft.command.list");
        setArgs("[uuids]");
    }

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
                                                                 .addExtra(new ChatComponent.Builder().setText(con.getProfile()
                                                                                                                  .getUuid()
                                                                                                                  .toString())
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
}
