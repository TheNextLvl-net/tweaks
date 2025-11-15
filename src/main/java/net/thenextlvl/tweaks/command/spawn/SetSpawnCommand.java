package net.thenextlvl.tweaks.command.spawn;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.RotationResolver;
import io.papermc.paper.math.FinePosition;
import io.papermc.paper.math.Rotation;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.model.LazyLocation;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jspecify.annotations.NullMarked;

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
                    return setSpawn(context, location.getWorld(), location, Rotation.rotation(location.getYaw(), location.getPitch()));
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
                    return setSpawn(context, world, position, Rotation.rotation(0, 0));
                });
    }

    private RequiredArgumentBuilder<CommandSourceStack, RotationResolver> rotation() {
        return Commands.argument("rotation", ArgumentTypes.rotation()).executes(context -> {
            var rotation = context.getArgument("rotation", RotationResolver.class);
            var resolver = context.getArgument("position", FinePositionResolver.class);
            var position = resolver.resolve(context.getSource());
            var world = context.getSource().getLocation().getWorld();
            return setSpawn(context, world, position, rotation.resolve(context.getSource()));
        }).then(world());
    }

    private RequiredArgumentBuilder<CommandSourceStack, World> world() {
        return Commands.argument("world", ArgumentTypes.world())
                .executes(context -> {
                    var rotation = context.getArgument("rotation", RotationResolver.class);
                    var resolver = context.getArgument("position", FinePositionResolver.class);
                    var position = resolver.resolve(context.getSource());
                    var world = context.getArgument("world", World.class);
                    return setSpawn(context, world, position, rotation.resolve(context.getSource()));
                });
    }

    private int setSpawn(CommandContext<CommandSourceStack> context, World world, FinePosition position, Rotation rotation) {
        var spawn = position.toLocation(world).setRotation(rotation);
        plugin.config().spawn.location = new LazyLocation(spawn);
        plugin.saveConfig();

        if (Boolean.TRUE.equals(world.getGameRuleValue(GameRule.SEND_COMMAND_FEEDBACK)))
            plugin.bundle().sendMessage(context.getSource().getSender(), "command.spawn.set",
                    Placeholder.parsed("world", world.getName()),
                    Formatter.number("x", position.x()),
                    Formatter.number("y", position.y()),
                    Formatter.number("z", position.z()),
                    Formatter.number("yaw", rotation.yaw()),
                    Formatter.number("pitch", rotation.pitch()));

        if (context.getSource().getSender() instanceof Player player)
            player.teleportAsync(spawn, PlayerTeleportEvent.TeleportCause.COMMAND);

        return Command.SINGLE_SUCCESS;
    }
}
