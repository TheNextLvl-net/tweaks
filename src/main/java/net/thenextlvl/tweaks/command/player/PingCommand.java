package net.thenextlvl.tweaks.command.player;

import core.api.placeholder.Placeholder;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

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
            sender.sendRichMessage(Messages.ping.others.message(locale, sender, ping, placeholder));
        } else player.sendRichMessage(Messages.ping.self.message(player.locale(), player, ping));
    }

    @Override
    protected @Nullable String getArgumentPermission(CommandSender sender, Player argument) {
        return sender.equals(argument) ? null : "tweaks.command.ping.others";
    }
}
