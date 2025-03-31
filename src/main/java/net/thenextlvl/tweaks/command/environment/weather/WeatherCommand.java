package net.thenextlvl.tweaks.command.environment.weather;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class WeatherCommand {
    private final TweaksPlugin plugin;

    public WeatherCommand(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().weather.command)
                .requires(stack -> stack.getSender().hasPermission("tweaks.command.weather"))
                .then(setWeather("clear", "tweaks.command.weather.sun", this::clear))
                .then(setWeather("rain", "tweaks.command.weather.rain", this::rain))
                .then(setWeather("thunder", "tweaks.command.weather.thunder", this::thunder))
                .build();
        registrar.register(command, "Gives you an item of your choice", plugin.commands().weather.aliases);
    }

    private LiteralArgumentBuilder<CommandSourceStack> setWeather(String literal, String permission, Executor executor) {
        return Commands.literal(literal)
                .requires(stack -> stack.getSender().hasPermission(permission))
                .then(Commands.argument("world", ArgumentTypes.world())
                        .then(Commands.argument("duration", ArgumentTypes.time(1))
                                .executes(context -> {
                                    var world = context.getArgument("world", World.class);
                                    var duration = context.getArgument("duration", int.class);
                                    executor.execute(context.getSource().getSender(), world, duration);
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .executes(context -> {
                            var world = context.getArgument("world", World.class);
                            executor.execute(context.getSource().getSender(), world, -1);
                            return Command.SINGLE_SUCCESS;
                        }))
                .executes(context -> {
                    var world = context.getSource().getLocation().getWorld();
                    executor.execute(context.getSource().getSender(), world, -1);
                    return Command.SINGLE_SUCCESS;
                });
    }

    private interface Executor {
        void execute(CommandSender sender, World world, int duration);
    }

    private void clear(CommandSender sender, World world, int duration) {
        setWeatherParameters(sender, world, false, false, duration, "command.weather.sun");
    }

    private void rain(CommandSender sender, World world, int duration) {
        setWeatherParameters(sender, world, true, false, duration, "command.weather.rain");
    }

    private void thunder(CommandSender sender, World world, int duration) {
        setWeatherParameters(sender, world, true, true, duration, "command.weather.thunder");
    }

    private void setWeatherParameters(CommandSender sender, World world, boolean rain, boolean thunder, int duration, String message) {
        world.setStorm(rain);
        world.setThundering(thunder);
        if (rain || thunder) world.setWeatherDuration(duration);
        else world.setClearWeatherDuration(duration);
        plugin.bundle().sendMessage(sender, message,
                Placeholder.parsed("duration", String.valueOf(duration)),
                Placeholder.parsed("world", world.getName()));
    }
}
