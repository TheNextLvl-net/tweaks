package net.thenextlvl.tweaks.command.server;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class LobbyCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().lobby().command())
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.lobby"))
                .executes(context -> {
                    var sender = (Player) context.getSource().getSender();
                    plugin.messenger().connect(sender, plugin.config().general().lobbyServerName());
                    return Command.SINGLE_SUCCESS;
                })
                .build();
        registrar.register(command, "Connect to the lobby server", plugin.commands().lobby().aliases());
    }
}
