package net.thenextlvl.tweaks.command.tpa;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import core.paper.command.CustomArgumentTypes;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
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
                .then(Commands.argument("player", CustomArgumentTypes.playerExact())
                        .suggests(new TPASuggestionProvider(plugin))
                        .executes(this::ask))
                .build();
        commands.register(command, "Request a player to teleport to you", plugin.commands().teleportHere.aliases);
    }

    private int ask(CommandContext<CommandSourceStack> context) {
        var sender = (Player) context.getSource().getSender();
        var player = context.getArgument("player", Player.class);
        return TPAskCommand.ask(plugin, sender, player, TPA_HERE) ? Command.SINGLE_SUCCESS : 0;
    }
}
