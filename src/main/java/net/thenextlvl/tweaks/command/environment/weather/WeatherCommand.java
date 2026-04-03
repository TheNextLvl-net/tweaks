package net.thenextlvl.tweaks.command.environment.weather;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.GameRules;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class WeatherCommand {
    private final TweaksPlugin plugin;

    public WeatherCommand(final TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(final Commands registrar) {
        final var command = Commands.literal(plugin.commands().weather.command)
                .requires(stack -> stack.getSender().hasPermission("tweaks.command.weather"))
                .then(setWeather("clear", "tweaks.command.weather.sun", this::clear))
                .then(setWeather("rain", "tweaks.command.weather.rain", this::rain))
                .then(setWeather("thunder", "tweaks.command.weather.thunder", this::thunder))
                .build();
        registrar.register(command, "Gives you an item of your choice", plugin.commands().weather.aliases);
    }

    private LiteralArgumentBuilder<CommandSourceStack> setWeather(final String literal, final String permission, final Executor executor) {
        return Commands.literal(literal)
                .requires(stack -> stack.getSender().hasPermission(permission))
                .then(Commands.argument("world", ArgumentTypes.world())
                        .then(Commands.argument("duration", ArgumentTypes.time(1))
                                .executes(context -> {
                                    final var world = context.getArgument("world", World.class);
                                    final var duration = context.getArgument("duration", int.class);
                                    executor.execute(context.getSource().getSender(), world, duration);
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .executes(context -> {
                            final var world = context.getArgument("world", World.class);
                            executor.execute(context.getSource().getSender(), world, -1);
                            return Command.SINGLE_SUCCESS;
                        }))
                .executes(context -> {
                    final var world = context.getSource().getLocation().getWorld();
                    executor.execute(context.getSource().getSender(), world, -1);
                    return Command.SINGLE_SUCCESS;
                });
    }

    private interface Executor {
        void execute(CommandSender sender, World world, int duration);
    }

    private void clear(final CommandSender sender, final World world, final int duration) {
        setWeatherParameters(sender, world, false, false, duration, "command.weather.sun");
    }

    private void rain(final CommandSender sender, final World world, final int duration) {
        setWeatherParameters(sender, world, true, false, duration, "command.weather.rain");
    }

    private void thunder(final CommandSender sender, final World world, final int duration) {
        setWeatherParameters(sender, world, true, true, duration, "command.weather.thunder");
    }

    private void setWeatherParameters(final CommandSender sender, final World world, final boolean rain, final boolean thunder, final int duration, final String message) {
        plugin.getServer().getGlobalRegionScheduler().run(plugin, task -> {
            world.setStorm(rain);
            world.setThundering(thunder);
            if (rain || thunder) world.setWeatherDuration(duration);
            else world.setClearWeatherDuration(duration);
        });
        if (Boolean.TRUE.equals(world.getGameRuleValue(GameRules.SEND_COMMAND_FEEDBACK)))
            plugin.bundle().sendMessage(sender, message,
                    Formatter.number("duration", duration),
                    Placeholder.parsed("world", world.getName()));
    }
}
