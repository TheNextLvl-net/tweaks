package net.thenextlvl.tweaks.command.tpa;

import com.mojang.brigadier.Command;
import core.paper.command.CustomArgumentTypes;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.suggestion.RequestSuggestionProvider;
import net.thenextlvl.tweaks.controller.TPAController.Request;
import net.thenextlvl.tweaks.controller.TPAController.RequestType;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import static net.thenextlvl.tweaks.controller.TPAController.RequestType.TPA;
import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.COMMAND;

@NullMarked
@RequiredArgsConstructor
public class TPAcceptCommand {
    private final TweaksPlugin plugin;

    public void register(Commands commands) {
        var command = Commands.literal(plugin.commands().teleportAccept().command())
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.tpa.accept"))
                .then(Commands.argument("player", CustomArgumentTypes.playerExact())
                        .suggests(new RequestSuggestionProvider(plugin))
                        .executes(context -> {
                            var sender = (Player) context.getSource().getSender();
                            var target = context.getArgument("player", Player.class);
                            var type = plugin.tpaController().getRequest(sender, target);
                            return accept(plugin, sender, target, type.map(Request::type).orElse(TPA));
                        }))
                .executes(context -> {
                    var sender = (Player) context.getSource().getSender();
                    var requests = plugin.tpaController().getRequests(sender);
                    var request = requests.isEmpty() ? null : requests.getFirst();
                    return accept(plugin, sender, request != null ? request.player() : null,
                            request != null ? request.type() : TPA);
                })
                .build();
        commands.register(command, "Accept a teleport request", plugin.commands().teleportAccept().aliases());
    }

    static int accept(TweaksPlugin plugin, Player sender, @Nullable Player player, RequestType type) {
        if (player == null || !plugin.tpaController().removeRequest(sender, player, type)) {
            var message = player != null ? "command.tpa.no-request" : "command.tpa.no-requests";
            plugin.bundle().sendMessage(sender, message,
                    Placeholder.parsed("player", player != null ? player.getName() : "null"));
            return 0;
        }

        plugin.bundle().sendMessage(sender, "command.tpa.accepted.self",
                Placeholder.parsed("player", player.getName()));
        plugin.bundle().sendMessage(player, "command.tpa.accepted",
                Placeholder.parsed("player", sender.getName()));

        if (type.equals(TPA)) teleport(plugin, player, sender); // sender accepts the tpa - teleport player to sender
        else teleport(plugin, sender, player); // sender accepts the tpahere - teleport sender to player

        return Command.SINGLE_SUCCESS;
    }

    private static void teleport(TweaksPlugin plugin, Player player, Player target) {
        plugin.teleportController().teleport(player, target.getLocation(), COMMAND).thenAccept(success -> {
            var message = success ? "command.tpa.teleported" : "command.teleport.cancelled";
            plugin.bundle().sendMessage(player, message, Placeholder.parsed("player", target.getName()));
        });
    }
}
