package net.thenextlvl.tweaks.command.environment;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.suggestion.WorldSuggestionProvider;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public abstract class WorldCommand {
    protected final TweaksPlugin plugin;

    public LiteralCommandNode<CommandSourceStack> create(String command, String permission) {
        return Commands.literal(command)
                .requires(stack -> stack.getSender().hasPermission(permission))
                .then(Commands.argument("world", ArgumentTypes.world())
                        .suggests(new WorldSuggestionProvider<>(plugin, this))
                        .executes(context -> {
                            var world = context.getArgument("world", World.class);
                            return execute(context, world);
                        }))
                .executes(context -> execute(context, context.getSource().getLocation().getWorld()))
                .build();
    }

    private int execute(CommandContext<CommandSourceStack> context, World world) {
        var sender = context.getSource().getSender();
        if (!isWorldAffected(world)) {
            plugin.bundle().sendMessage(sender, "command.world.excluded",
                    Placeholder.parsed("world", world.getName()));
            return 0;
        }
        plugin.getServer().getGlobalRegionScheduler().run(plugin, task -> execute(sender, world));
        return Command.SINGLE_SUCCESS;
    }

    protected abstract void execute(CommandSender sender, World world);

    public abstract boolean isWorldAffected(World world);
}
