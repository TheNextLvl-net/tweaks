package net.thenextlvl.tweaks.command.player;

import com.mojang.brigadier.exceptions.BuiltInExceptions;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import core.paper.command.CustomArgumentTypes;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
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
                .then(Commands.argument("player", CustomArgumentTypes.playerExact())
                        .requires(stack -> stack.getSender().hasPermission(permissionOther))
                        .executes(context -> {
                            var player = context.getArgument("player", Player.class);
                            return execute(context.getSource().getSender(), player);
                        }))
                .executes(context -> execute(context.getSource().getSender()))
                .build();
    }

    protected int execute(CommandSender sender) throws CommandSyntaxException {
        if (sender instanceof Player player) return execute(sender, player);
        throw new BuiltInExceptions().dispatcherUnknownCommand().create();
    }

    protected abstract int execute(CommandSender sender, Player player) throws CommandSyntaxException;
}
