package net.thenextlvl.tweaks.command.tpa;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.suggestion.TPASuggestionProvider;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import static net.thenextlvl.tweaks.controller.TPAController.RequestType.TPA_HERE;

@NullMarked
public class TPAHereCommand {
    private final TweaksPlugin plugin;

    public TPAHereCommand(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Commands commands) {
        var command = Commands.literal(plugin.commands().teleportHere.command)
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.tpa.here"))
                .then(Commands.argument("player", ArgumentTypes.player())
                        .suggests(new TPASuggestionProvider(plugin))
                        .executes(this::ask))
                .build();
        commands.register(command, "Request a player to teleport to you", plugin.commands().teleportHere.aliases);
    }

    private int ask(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var sender = (Player) context.getSource().getSender();
        var resolver = context.getArgument("player", PlayerSelectorArgumentResolver.class);
        var player = resolver.resolve(context.getSource()).getFirst();
        return TPAskCommand.ask(plugin, sender, player, TPA_HERE) ? Command.SINGLE_SUCCESS : 0;
    }
}
