package net.thenextlvl.tweaks.command.environment.time;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

import java.util.function.Function;

@NullMarked
public class TimeCommand {
    private final TweaksPlugin plugin;

    public TimeCommand(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().time.command)
                .requires(stack -> stack.getSender().hasPermission("tweaks.command.time"))
                .then(Commands.literal("set")
                        .then(setTime("afternoon", "tweaks.command.time.afternoon", 9000))
                        .then(setTime("day", "tweaks.command.time.day", 1000))
                        .then(setTime("midnight", "tweaks.command.time.midnight", 18000))
                        .then(setTime("morning", "tweaks.command.time.morning", 0))
                        .then(setTime("night", "tweaks.command.time.night", 13000))
                        .then(setTime("noon", "tweaks.command.time.noon", 6000))
                        .then(setTime("sunrise", "tweaks.command.time.sunrise", 23000))
                        .then(setTime("sunset", "tweaks.command.time.sunset", 12000))
                        .then(setTime()))
                .then(Commands.literal("add")
                        .requires(stack -> stack.getSender().hasPermission("tweaks.command.time.add"))
                        .then(Commands.argument("time", ArgumentTypes.time())
                                .then(Commands.argument("world", ArgumentTypes.world())
                                        .executes(context -> addTime(context, context.getArgument("world", World.class))))
                                .executes(context -> addTime(context, context.getSource().getLocation().getWorld()))))
                .then(Commands.literal("query")
                        .requires(stack -> stack.getSender().hasPermission("tweaks.command.time.query"))
                        .then(query("day", world -> world.getFullTime() / 24000L % 2147483647L))
                        .then(query("daytime", world -> world.getFullTime() % 24000L))
                        .then(query("gametime", world -> world.getGameTime() % 2147483647L)))
                .build();
        registrar.register(command, "Manage the time on your server", plugin.commands().time.aliases);
    }

    private LiteralArgumentBuilder<CommandSourceStack> query(String literal, Function<World, Long> function) {
        return Commands.literal(literal)
                .then(Commands.argument("world", ArgumentTypes.world())
                        .executes(context -> query(context, context.getArgument("world", World.class), function)))
                .executes(context -> query(context, context.getSource().getLocation().getWorld(), function));
    }

    private RequiredArgumentBuilder<CommandSourceStack, Integer> setTime() {
        return Commands.argument("time", ArgumentTypes.time())
                .requires(stack -> stack.getSender().hasPermission("tweaks.command.time.set"))
                .then(Commands.argument("world", ArgumentTypes.world())
                        .executes(context -> {
                            var time = context.getArgument("time", int.class);
                            var world = context.getArgument("world", World.class);
                            return setTime(context, time, world);
                        }))
                .executes(context -> {
                    var time = context.getArgument("time", int.class);
                    var world = context.getSource().getLocation().getWorld();
                    return setTime(context, time, world);
                });
    }

    private LiteralArgumentBuilder<CommandSourceStack> setTime(String literal, String permission, int ticks) {
        return Commands.literal(literal)
                .requires(stack -> stack.getSender().hasPermission(permission))
                .then(Commands.argument("world", ArgumentTypes.world())
                        .executes(context -> setTime(context, ticks, context.getArgument("world", World.class))))
                .executes(context -> setTime(context, ticks, context.getSource().getLocation().getWorld()));
    }

    private int setTime(CommandContext<CommandSourceStack> context, long ticks, World world) {
        var sender = context.getSource().getSender();
        plugin.bundle().sendMessage(sender, "command.time.set",
                Formatter.number("time", ticks),
                Placeholder.parsed("world", world.getName()));
        world.setTime(ticks);
        return Command.SINGLE_SUCCESS;
    }

    private int addTime(CommandContext<CommandSourceStack> context, World world) {
        var time = context.getArgument("time", int.class);
        return setTime(context, world.getTime() + time, world);
    }

    private int query(CommandContext<CommandSourceStack> context, World world, Function<World, Long> function) {
        var sender = context.getSource().getSender();
        plugin.bundle().sendMessage(sender, "command.time.query",
                Formatter.number("time", function.apply(world)),
                Placeholder.parsed("world", world.getName()));
        return Command.SINGLE_SUCCESS;
    }

    // todo: respect paper-global.yml -> commands.time-command-affects-all-worlds
}
