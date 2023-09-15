package net.defekt.minecraft.starbox.command.impl.admin;

import net.defekt.minecraft.starbox.command.Command;
import net.defekt.minecraft.starbox.data.ChatComponent;
import net.defekt.minecraft.starbox.network.PlayerConnection;
import net.defekt.minecraft.starbox.world.World;

public class WeatherCommand extends Command {
    public WeatherCommand() {
        super("weather", "minecraft.command.weather");
        setArgs("(clear|rain|thunder)");
    }

    @Override
    public boolean execute(PlayerConnection player, String command, String[] args) {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "thunder": {
                    player.getWorld().setWeather(World.Weather.THUNDER);
                    player.sendMessage(new ChatComponent.Builder().setTranslate("commands.weather.set.thunder")
                                                                  .build());
                    break;
                }
                case "rain": {
                    player.getWorld().setWeather(World.Weather.RAIN);
                    player.sendMessage(new ChatComponent.Builder().setTranslate("commands.weather.set.rain").build());
                    break;
                }
                case "clear": {
                    player.getWorld().setWeather(World.Weather.CLEAR);
                    player.sendMessage(new ChatComponent.Builder().setTranslate("commands.weather.set.clear").build());
                    break;
                }
                default: {
                    printUsage(player);
                    break;
                }
            }
        }
        return true;
    }
}
