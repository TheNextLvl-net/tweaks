package net.thenextlvl.tweaks.command.warp;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.suggestion.WarpSuggestionProvider;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.COMMAND;

@NullMarked
@RequiredArgsConstructor
public class WarpCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().warp().command())
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.warp"))
                .then(Commands.argument("name", StringArgumentType.string())
                        .suggests(new WarpSuggestionProvider(plugin))
                        .executes(this::warp))
                .build();
        registrar.register(command, "Warp to a location", plugin.commands().warp().aliases());
    }

    private int warp(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        var name = context.getArgument("name", String.class);
        plugin.warpController().getWarp(name).thenAccept(warp -> {
            if (warp != null) plugin.teleportController().teleport(player, warp, COMMAND).thenAccept(success -> {
                var message = success ? "command.warp" : "command.teleport.cancelled";
                plugin.bundle().sendMessage(player, message, Placeholder.parsed("name", name));
            });
            else plugin.bundle().sendMessage(player, "command.warp.unknown", Placeholder.parsed("name", name));
        });
        return Command.SINGLE_SUCCESS;
    }
}
