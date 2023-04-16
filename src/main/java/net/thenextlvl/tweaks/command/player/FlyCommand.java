package net.thenextlvl.tweaks.command.player;

import core.api.placeholder.Placeholder;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(
        name = "fly",
        usage = "/<command> (player)",
        description = "toggle your own or someone else's fly state",
        permission = "tweaks.command.fly",
        aliases = {"flight"}
)
public class FlyCommand extends PlayerCommand {

    @Override
    protected void execute(CommandSender sender, Player player) {
        player.setAllowFlight(!player.getAllowFlight());

        var messageSelf = player.getAllowFlight() ? Messages.ENABLED_FLIGHT_SELF : Messages.DISABLED_FLIGHT_SELF;
        var messageOthers = player.getAllowFlight() ? Messages.ENABLED_FLIGHT_OTHERS : Messages.DISABLED_FLIGHT_OTHERS;

        player.sendRichMessage(messageSelf.message(player.locale()));
        if (player == sender) return;
        var locale = sender instanceof Player p ? p.locale() : Messages.ENGLISH;
        var placeholder = Placeholder.<CommandSender>of("player", player.getName());
        sender.sendRichMessage(messageOthers.message(locale, placeholder));
    }
}
