package net.thenextlvl.tweaks.command.environment.time;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.World;

import java.util.function.Function;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class TimeCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().time().command())
                .requires(stack -> stack.getSender().hasPermission("tweaks.command.time"))
                .then(Commands.literal("set")
                        .then(setTime())
                        .then(setTime("afternoon", 9000))
                        .then(setTime("day", 1000))
                        .then(setTime("midnight", 18000))
                        .then(setTime("morning", 0))
                        .then(setTime("night", 13000))
                        .then(setTime("noon", 6000))
                        .then(setTime("sunrise", 23000))
                        .then(setTime("sunset", 12000)))
                .then(Commands.literal("add")
                        .then(Commands.argument("time", ArgumentTypes.time())
                                .then(Commands.argument("world", ArgumentTypes.world())
                                        .executes(context -> addTime(context, context.getArgument("world", World.class))))
                                .executes(context -> addTime(context, context.getSource().getLocation().getWorld()))))
                .then(Commands.literal("query")
                        .then(query("day", world -> world.getFullTime() / 24000L % 2147483647L))
                        .then(query("daytime", world -> world.getFullTime() % 24000L))
                        .then(query("gametime", world -> world.getGameTime() % 2147483647L)))
                .build();
        registrar.register(command, "Manage the time on your server", plugin.commands().time().aliases());
    }

    private LiteralArgumentBuilder<CommandSourceStack> query(String literal, Function<World, Long> function) {
        return Commands.literal(literal)
                .then(Commands.argument("world", ArgumentTypes.world())
                        .executes(context -> query(context, context.getArgument("world", World.class), function)))
                .executes(context -> query(context, context.getSource().getLocation().getWorld(), function));
    }

    private RequiredArgumentBuilder<CommandSourceStack, Integer> setTime() {
        return Commands.argument("time", ArgumentTypes.time())
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

    private LiteralArgumentBuilder<CommandSourceStack> setTime(String literal, int ticks) {
        return Commands.literal(literal)
                .then(Commands.argument("world", ArgumentTypes.world())
                        .executes(context -> setTime(context, ticks, context.getArgument("world", World.class))))
                .executes(context -> setTime(context, ticks, context.getSource().getLocation().getWorld()));
    }

    private int setTime(CommandContext<CommandSourceStack> context, long ticks, World world) {
        var sender = context.getSource().getSender();
        plugin.bundle().sendMessage(sender, "command.time.set",
                Placeholder.parsed("time", String.valueOf(ticks)),
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
                Placeholder.parsed("time", String.valueOf(function.apply(world))),
                Placeholder.parsed("world", world.getName()));
        return Command.SINGLE_SUCCESS;
    }
}
