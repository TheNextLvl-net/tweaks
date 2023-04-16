package net.thenextlvl.tweaks.command.player;

import core.api.placeholder.Placeholder;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(
        name = "ping",
        usage = "/<command> (player)",
        description = "see your own or someone else's latency",
        permission = "tweaks.command.ping",
        aliases = {"latency"}
)
public class PingCommand extends PlayerCommand {

    @Override
    protected void execute(CommandSender sender, Player player) {
        var ping = Placeholder.<CommandSender>of("ping", player.getPing());
        if (sender != player) {
            var locale = sender instanceof Player p ? p.locale() : Messages.ENGLISH;
            var placeholder = Placeholder.<CommandSender>of("player", player.getName());
            sender.sendPlainMessage(Messages.PING_OTHERS.message(locale, sender, ping, placeholder));
        } else player.sendPlainMessage(Messages.PING_SELF.message(player.locale(), player, ping));
    }
}
