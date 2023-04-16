package net.thenextlvl.tweaks.command.api;

import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.command.CommandSender;

import java.util.Locale;

public class NoBackLocationException extends CommandException {

    @Override
    public void handle(Locale locale, CommandSender sender) {
        sender.sendRichMessage(Messages.BACK_EMPTY.message(locale, sender));
    }
}
