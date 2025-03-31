package net.thenextlvl.tweaks.command.spawn;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.math.FinePosition;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jspecify.annotations.NullMarked;

import java.text.DecimalFormat;
import java.util.Locale;

@NullMarked
public class SetSpawnCommand {
    private final TweaksPlugin plugin;

    public SetSpawnCommand(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().setSpawn.command)
                .requires(stack -> stack.getSender().hasPermission("tweaks.command.setspawn"))
                .then(position()).executes(context -> {
                    var location = context.getSource().getLocation();
                    return setSpawn(context, location.getWorld(), location, location.getYaw(), location.getPitch());
                }).build();
        registrar.register(command, "Set the spawn location", plugin.commands().setSpawn.aliases);
    }

    private RequiredArgumentBuilder<CommandSourceStack, FinePositionResolver> position() {
        return Commands.argument("position", ArgumentTypes.finePosition())
                .then(rotation())
                .executes(context -> {
                    var resolver = context.getArgument("position", FinePositionResolver.class);
                    var position = resolver.resolve(context.getSource());
                    var world = context.getSource().getLocation().getWorld();
                    return setSpawn(context, world, position, 0, 0);
                });
    }

    private RequiredArgumentBuilder<CommandSourceStack, Float> rotation() {
        return Commands.argument("yaw", FloatArgumentType.floatArg(-180, 180))
                .then(Commands.argument("pitch", FloatArgumentType.floatArg(-180, 180))
                        .then(world())
                        .executes(context -> {
                            var yaw = context.getArgument("yaw", float.class);
                            var pitch = context.getArgument("pitch", float.class);
                            var resolver = context.getArgument("position", FinePositionResolver.class);
                            var position = resolver.resolve(context.getSource());
                            var world = context.getSource().getLocation().getWorld();
                            return setSpawn(context, world, position, yaw, pitch);
                        }));
    }

    private RequiredArgumentBuilder<CommandSourceStack, World> world() {
        return Commands.argument("world", ArgumentTypes.world())
                .executes(context -> {
                    var yaw = context.getArgument("yaw", float.class);
                    var pitch = context.getArgument("pitch", float.class);
                    var resolver = context.getArgument("position", FinePositionResolver.class);
                    var position = resolver.resolve(context.getSource());
                    var world = context.getArgument("world", World.class);
                    return setSpawn(context, world, position, yaw, pitch);
                });
    }

    private int setSpawn(CommandContext<CommandSourceStack> context, World world, FinePosition position, float yaw, float pitch) {
        var locale = context.getSource().getSender() instanceof Player player ? player.locale() : Locale.US;
        var formatter = DecimalFormat.getInstance(locale);

        plugin.config().spawn.location = position.toLocation(world);
        plugin.saveConfig();

        plugin.bundle().sendMessage(context.getSource().getSender(), "command.spawn.set",
                Placeholder.parsed("world", world.getName()),
                Placeholder.parsed("x", formatter.format(position.x())),
                Placeholder.parsed("y", formatter.format(position.y())),
                Placeholder.parsed("z", formatter.format(position.z())),
                Placeholder.parsed("yaw", formatter.format(yaw)),
                Placeholder.parsed("pitch", formatter.format(pitch)));

        if (context.getSource().getSender() instanceof Player player)
            player.teleportAsync(position.toLocation(world), PlayerTeleportEvent.TeleportCause.COMMAND);

        return Command.SINGLE_SUCCESS;
    }
}
