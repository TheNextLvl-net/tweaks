package net.thenextlvl.tweaks.command.tpa;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import core.paper.command.CustomArgumentTypes;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.suggestion.TPASuggestionProvider;
import net.thenextlvl.tweaks.controller.TPAController.RequestType;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static net.thenextlvl.tweaks.controller.TPAController.RequestType.TPA;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class TPAskCommand {
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final TweaksPlugin plugin;

    public void register(Commands commands) {
        var command = Commands.literal(plugin.commands().teleportAsk().command())
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.tpa"))
                .then(Commands.argument("player", CustomArgumentTypes.playerExact())
                        .suggests(new TPASuggestionProvider(plugin))
                        .executes(this::ask))
                .build();
        commands.register(command, "Ask a player to be teleported", plugin.commands().teleportAsk().aliases());
    }

    private int ask(CommandContext<CommandSourceStack> context) {
        var sender = (Player) context.getSource().getSender();
        var player = context.getArgument("player", Player.class);
        return ask(plugin, sender, player, TPA) ? Command.SINGLE_SUCCESS : 0;
    }

    static boolean ask(TweaksPlugin plugin, Player sender, Player player, RequestType type) {
        if (sender.equals(player)) {
            plugin.bundle().sendMessage(sender, "command.tpa.self");
            return false;
        }

        if (plugin.dataController().isTpaToggled(player)) {
            plugin.bundle().sendMessage(sender, "command.tpa.toggled",
                    Placeholder.parsed("player", player.getName()));
            return false;
        }

        var success = plugin.tpaController().addRequest(player, sender, type);
        plugin.bundle().sendMessage(sender, success ? type.outgoingMessage() : "command.tpa.sent",
                Placeholder.parsed("player", player.getName()));

        if (!success) return false;

        executor.schedule(() -> plugin.tpaController().expireRequest(player, sender, type),
                plugin.config().teleport().tpaTimeout(), TimeUnit.MILLISECONDS);

        plugin.bundle().sendMessage(player, type.incomingMessage(),
                Placeholder.parsed("player", sender.getName()),
                Placeholder.styling("accept", ClickEvent.callback(audience ->
                                // invert players for execution
                                // player accepts sender's tpa
                                TPAcceptCommand.accept(plugin, player, sender, type),
                        ClickCallback.Options.builder().lifetime(
                                Duration.ofMillis(plugin.config().teleport().tpaTimeout())
                        ).uses(1).build())),
                Placeholder.styling("deny", ClickEvent.callback(audience ->
                                // invert players for execution
                                // player declines sender's tpa
                                TPADenyCommand.deny(plugin, player, sender, type),
                        ClickCallback.Options.builder().lifetime(
                                Duration.ofMillis(plugin.config().teleport().tpaTimeout())
                        ).uses(1).build())),
                Placeholder.parsed("time", String.valueOf(TimeUnit.MILLISECONDS.toSeconds(
                        plugin.config().teleport().tpaTimeout())
                )));
        return true;
    }
}
