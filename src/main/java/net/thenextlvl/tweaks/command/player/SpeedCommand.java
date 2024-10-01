package net.thenextlvl.tweaks.command.player;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class SpeedCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().speed().command())
                // todo: speed reset
                .requires(stack -> stack.getSender().hasPermission("tweaks.command.speed"))
                .then(Commands.argument("speed", IntegerArgumentType.integer(-10, 10))
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .requires(stack -> stack.getSender().hasPermission("tweaks.command.speed.others"))
                                .executes(context -> {
                                    var player = context.getArgument("player", PlayerSelectorArgumentResolver.class);
                                    return speed(context, player.resolve(context.getSource()).getFirst());
                                }))
                        .executes(context -> {
                            var sender = context.getSource().getSender();
                            if (sender instanceof Player player) return speed(context, player);
                            plugin.bundle().sendMessage(sender, "command.sender");
                            return 0;
                        }))
                .build();
        registrar.register(command, "change your own or someone else's walk or fly speed",
                plugin.commands().speed().aliases());
    }

    private int speed(CommandContext<CommandSourceStack> context, Player player) {
        var sender = context.getSource().getSender();
        var speed = context.getArgument("speed", int.class);

        if (player.isFlying()) player.setFlySpeed(speed / 10f);
        else player.setWalkSpeed(speed / 10f);

        var messageSelf = player.isFlying() ? "command.speed.fly.changed.self" : "command.speed.walk.changed.self";
        var messageOthers = player.isFlying() ? "command.speed.fly.changed.others" : "command.speed.walk.changed.others";


        plugin.bundle().sendMessage(sender, messageSelf, Placeholder.parsed("speed", String.valueOf(speed)));
        if (!player.equals(sender)) plugin.bundle().sendMessage(sender, messageOthers,
                Placeholder.parsed("speed", String.valueOf(speed)),
                Placeholder.parsed("player", player.getName()));
        return 0;
    }
}
