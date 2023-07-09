package net.thenextlvl.tweaks.command.api;

import core.api.placeholder.Placeholder;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Locale;

@RequiredArgsConstructor
public class PlayerNotAffectedException extends CommandException {
    private final OfflinePlayer player;

    @Override
    public void handle(Locale locale, CommandSender sender) {
        sender.sendRichMessage(Messages.PLAYER_NOT_AFFECTED.message(locale, Placeholder.of("player", player.getName())));
    }
}
