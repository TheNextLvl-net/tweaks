package net.thenextlvl.tweaks.command.tpa;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.suggestion.RequestSuggestionProvider;
import net.thenextlvl.tweaks.controller.TPAController;
import net.thenextlvl.tweaks.controller.TPAController.RequestType;
import org.bukkit.GameRules;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import static net.thenextlvl.tweaks.controller.TPAController.RequestType.TPA;

@NullMarked
public class TPADenyCommand {
    private final TweaksPlugin plugin;

    public TPADenyCommand(final TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(final Commands commands) {
        final var command = Commands.literal(plugin.commands().teleportDeny.command)
                .requires(stack -> stack.getSender() instanceof final Player player
                                   && player.hasPermission("tweaks.command.tpa.deny"))
                .then(Commands.argument("player", ArgumentTypes.player())
                        .suggests(new RequestSuggestionProvider(plugin))
                        .executes(context -> {
                            final var sender = (Player) context.getSource().getSender();
                            final var resolver = context.getArgument("player", PlayerSelectorArgumentResolver.class);
                            final var target = resolver.resolve(context.getSource()).getFirst();
                            final var type = plugin.tpaController().getRequest(sender, target);
                            return deny(plugin, sender, target, type.map(TPAController.Request::type).orElse(TPA));
                        }))
                .executes(context -> {
                    final var sender = (Player) context.getSource().getSender();
                    final var requests = plugin.tpaController().getRequests(sender);
                    final var request = requests.isEmpty() ? null : requests.getFirst();
                    return deny(plugin, sender, request != null ? request.player() : null,
                            request != null ? request.type() : TPA);
                })
                .build();
        commands.register(command, "Deny a teleport request", plugin.commands().teleportDeny.aliases);
    }

    static int deny(final TweaksPlugin plugin, final Player sender, @Nullable final Player player, final RequestType type) {
        final var success = player != null && plugin.tpaController().removeRequest(sender, player, type);
        final var message = success ? "command.tpa.denied.self"
                : player != null ? "command.tpa.no-request"
                : "command.tpa.no-requests";
        if (Boolean.TRUE.equals(sender.getWorld().getGameRuleValue(GameRules.SEND_COMMAND_FEEDBACK)) || !success)
            plugin.bundle().sendMessage(sender, message,
                    Placeholder.parsed("player", player != null ? player.getName() : "null"));
        if (player != null && success) plugin.bundle().sendMessage(player, "command.tpa.denied",
                Placeholder.parsed("player", sender.getName()));
        return player != null ? Command.SINGLE_SUCCESS : 0;
    }
}
