package net.thenextlvl.tweaks.command.server;

import core.paper.messenger.PluginMessenger;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.CommandSenderException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(
        name = "lobby",
        description = "connect to the lobby",
        aliases = {"l", "hub"}
)
@RequiredArgsConstructor
public class LobbyCommand implements CommandExecutor {
    private final TweaksPlugin plugin;
    private final PluginMessenger messenger;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) throw new CommandSenderException();
        messenger.connect(player, plugin.config().serverConfig().lobbyServerName());
        return true;
    }
}
