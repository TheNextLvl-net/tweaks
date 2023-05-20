package net.thenextlvl.tweaks.command.player;

import core.api.placeholder.Placeholder;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(
        name = "god",
        usage = "/<command> (player)",
        description = "make you or someone else invulnerable",
        permission = "tweaks.command.god"
)
public class GodCommand extends PlayerCommand {
    @Override
    protected void execute(CommandSender sender, Player player) {
        player.setInvulnerable(!player.isInvulnerable());

        var messageSelf = player.isInvulnerable() ? Messages.GOD_MODE_ACTIVE_SELF : Messages.GOD_MODE_INACTIVE_SELF;
        var messageOthers = player.isInvulnerable() ? Messages.GOD_MODE_ACTIVE_OTHERS : Messages.GOD_MODE_INACTIVE_OTHERS;

        player.sendRichMessage(messageSelf.message(player.locale()));
        if (player == sender) return;
        var locale = sender instanceof Player p ? p.locale() : Messages.ENGLISH;
        var placeholder = Placeholder.<CommandSender>of("player", player.getName());
        sender.sendRichMessage(messageOthers.message(locale, placeholder));
    }
}
