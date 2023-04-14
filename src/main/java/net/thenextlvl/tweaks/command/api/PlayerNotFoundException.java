package net.thenextlvl.tweaks.command.api;

import core.api.placeholder.Placeholder;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.command.CommandSender;

import java.util.Locale;

@RequiredArgsConstructor
public class PlayerNotFoundException extends CommandException {
    private final String input;

    @Override
    protected void handle(Locale locale, CommandSender sender) {
        var placeholder = Placeholder.<CommandSender>of("player", input);
        sender.sendPlainMessage(Messages.PLAYER_NOT_ONLINE.message(locale, sender, placeholder));
    }
}
