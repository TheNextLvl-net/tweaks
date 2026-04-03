package net.thenextlvl.tweaks.command.player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
abstract class PlayersCommand {
    protected final TweaksPlugin plugin;

    PlayersCommand(final TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public LiteralCommandNode<CommandSourceStack> create(final String name, final String permission, final String permissionOther) {
        return Commands.literal(name)
                .requires(stack -> stack.getSender().hasPermission(permission))
                .then(Commands.argument("players", ArgumentTypes.players())
                        .requires(stack -> stack.getSender().hasPermission(permissionOther))
                        .executes(context -> {
                            final var players = context.getArgument("players", PlayerSelectorArgumentResolver.class);
                            return execute(context.getSource().getSender(), players.resolve(context.getSource()));
                        }))
                .executes(context -> {
                    final var sender = context.getSource().getSender();
                    if (sender instanceof final Player player) return execute(sender, List.of(player));
                    plugin.bundle().sendMessage(sender, "command.sender");
                    return 0;
                })
                .build();
    }

    protected int execute(final CommandSender sender, final List<Player> players) {
        players.forEach(player -> execute(sender, player));
        return Command.SINGLE_SUCCESS;
    }

    protected abstract void execute(CommandSender sender, Player player);
}
