package net.thenextlvl.tweaks.command.player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
@RequiredArgsConstructor
abstract class EntitiesCommand {
    protected final TweaksPlugin plugin;

    public LiteralCommandNode<CommandSourceStack> create(String name, String permission, String permissionOther) {
        return Commands.literal(name)
                .requires(stack -> stack.getSender().hasPermission(permission))
                .then(Commands.argument("targets", ArgumentTypes.entities())
                        .requires(stack -> stack.getSender().hasPermission(permissionOther))
                        .executes(context -> {
                            var targets = context.getArgument("targets", EntitySelectorArgumentResolver.class);
                            return execute(context.getSource().getSender(), targets.resolve(context.getSource()));
                        }))
                .executes(context -> {
                    var sender = context.getSource().getSender();
                    if (sender instanceof Player player) return execute(sender, List.of(player));
                    plugin.bundle().sendMessage(sender, "command.sender");
                    return 0;
                })
                .build();
    }

    protected int execute(CommandSender sender, List<Entity> targets) {
        targets.forEach(target -> execute(sender, target));
        return Command.SINGLE_SUCCESS;
    }

    protected abstract void execute(CommandSender sender, Entity entity);
}
