package net.thenextlvl.tweaks.command.player;

import net.thenextlvl.tweaks.command.api.CommandInfo;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(
        name = "ping",
        usage = "/<command> (player)",
        description = "see your own or someone else's latency",
        permission = "tweaks.command.ping"
)
public class PingCommand extends PlayerCommand {

    @Override
    protected void execute(CommandSender sender, Player player) {
        int ping = player.getPing();
        if (sender == player) {
            // TODO: Your ping is ...ms
        } else {
            // TODO: ...'s ping is ...ms
        }
    }
}
