package net.thenextlvl.tweaks.command.tpa;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.suggestion.TPASuggestionProvider;
import net.thenextlvl.tweaks.controller.TPAController.RequestType;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static net.thenextlvl.tweaks.controller.TPAController.RequestType.TPA;

@NullMarked
public class TPAskCommand {
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final TweaksPlugin plugin;

    public TPAskCommand(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Commands commands) {
        var command = Commands.literal(plugin.commands().teleportAsk.command)
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.tpa"))
                .then(Commands.argument("player", ArgumentTypes.player())
                        .suggests(new TPASuggestionProvider(plugin))
                        .executes(this::ask))
                .build();
        commands.register(command, "Request to teleport to a player", plugin.commands().teleportAsk.aliases);
    }

    private int ask(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var sender = (Player) context.getSource().getSender();
        var resolver = context.getArgument("player", PlayerSelectorArgumentResolver.class);
        var player = resolver.resolve(context.getSource()).getFirst();
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
        if (Boolean.TRUE.equals(player.getWorld().getGameRuleValue(GameRule.SEND_COMMAND_FEEDBACK)) || !success)
            plugin.bundle().sendMessage(sender, success ? type.outgoingMessage() : "command.tpa.sent",
                    Placeholder.parsed("player", player.getName()));

        if (!success) return false;

        executor.schedule(() -> plugin.tpaController().expireRequest(player, sender, type),
                plugin.config().teleport.tpaTimeout, TimeUnit.MILLISECONDS);

        plugin.bundle().sendMessage(player, type.incomingMessage(),
                Placeholder.parsed("player", sender.getName()),
                Placeholder.styling("accept", ClickEvent.callback(audience ->
                                // invert players for execution
                                // player accepts sender's tpa
                                TPAcceptCommand.accept(plugin, player, sender, type),
                        ClickCallback.Options.builder().lifetime(
                                Duration.ofMillis(plugin.config().teleport.tpaTimeout)
                        ).uses(1).build())),
                Placeholder.styling("deny", ClickEvent.callback(audience ->
                                // invert players for execution
                                // player declines sender's tpa
                                TPADenyCommand.deny(plugin, player, sender, type),
                        ClickCallback.Options.builder().lifetime(
                                Duration.ofMillis(plugin.config().teleport.tpaTimeout)
                        ).uses(1).build())),
                Formatter.number("time", TimeUnit.MILLISECONDS.toSeconds(plugin.config().teleport.tpaTimeout)));
        return true;
    }
}
