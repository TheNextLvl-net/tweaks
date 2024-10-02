package net.thenextlvl.tweaks.command.player;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
abstract class PlayerCommand {
    protected final TweaksPlugin plugin;

    public LiteralCommandNode<CommandSourceStack> create(String name, String permission, String permissionOther) {
        return Commands.literal(name)
                .requires(stack -> stack.getSender().hasPermission(permission))
                .then(Commands.argument("player", ArgumentTypes.player())
                        .requires(stack -> stack.getSender().hasPermission(permissionOther))
                        .executes(context -> {
                            var players = context.getArgument("player", PlayerSelectorArgumentResolver.class);
                            return execute(context.getSource().getSender(), players.resolve(context.getSource()).getFirst());
                        }))
                .executes(context -> execute(context.getSource().getSender()))
                .build();
    }

    protected int execute(CommandSender sender) {
        if (sender instanceof Player player) return execute(sender, player);
        plugin.bundle().sendMessage(sender, "command.sender");
        return 0;
    }

    protected abstract int execute(CommandSender sender, Player player);
}
