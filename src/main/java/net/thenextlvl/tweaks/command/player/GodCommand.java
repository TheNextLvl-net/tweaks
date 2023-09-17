package net.thenextlvl.tweaks.command.player;

import core.api.placeholder.Placeholder;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

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

        var messageSelf = player.isInvulnerable() ? Messages.god.active.self : Messages.god.inactive.self;
        var messageOthers = player.isInvulnerable() ? Messages.god.active.others : Messages.god.inactive.others;

        player.sendRichMessage(messageSelf.message(player.locale()));
        if (player == sender) return;
        var locale = sender instanceof Player p ? p.locale() : Messages.ENGLISH;
        var placeholder = Placeholder.<CommandSender>of("player", player.getName());
        sender.sendRichMessage(messageOthers.message(locale, placeholder));
    }

    @Override
    protected @Nullable String getArgumentPermission(CommandSender sender, Player argument) {
        return sender.equals(argument) ? null : "tweaks.command.god.others";
    }
}
