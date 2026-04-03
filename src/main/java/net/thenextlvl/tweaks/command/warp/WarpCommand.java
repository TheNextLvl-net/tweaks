package net.thenextlvl.tweaks.command.warp;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.suggestion.WarpSuggestionProvider;
import org.bukkit.GameRules;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.COMMAND;

@NullMarked
public class WarpCommand {
    private final TweaksPlugin plugin;

    public WarpCommand(final TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(final Commands registrar) {
        final var command = Commands.literal(plugin.commands().warp.command)
                .requires(stack -> stack.getSender() instanceof final Player player
                                   && player.hasPermission("tweaks.command.warp"))
                .then(Commands.argument("name", StringArgumentType.string())
                        .suggests(new WarpSuggestionProvider(plugin))
                        .executes(this::warp))
                .build();
        registrar.register(command, "Warp to a location", plugin.commands().warp.aliases);
    }

    private int warp(final CommandContext<CommandSourceStack> context) {
        final var player = (Player) context.getSource().getSender();
        final var name = context.getArgument("name", String.class);
        plugin.warpController().getWarp(name).thenAccept(warp -> warp.ifPresentOrElse(location ->
                plugin.teleportController().teleport(player, location, COMMAND).thenAccept(success -> {
                    final var message = success ? "command.warp" : "command.teleport.cancelled";
                    final var world = context.getSource().getLocation().getWorld();
                    if (Boolean.FALSE.equals(world.getGameRuleValue(GameRules.SEND_COMMAND_FEEDBACK)) && success) return;
                    plugin.bundle().sendMessage(player, message, Placeholder.parsed("name", name));
                }), () -> plugin.bundle().sendMessage(player, "command.warp.unknown", Placeholder.parsed("name", name))));
        return Command.SINGLE_SUCCESS;
    }
}
