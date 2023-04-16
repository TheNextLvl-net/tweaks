package net.thenextlvl.tweaks.command.api;

import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.command.CommandSender;

import java.util.Locale;

public class CommandSenderException extends CommandException {
    @Override
    public void handle(Locale locale, CommandSender sender) {
        sender.sendMessage(Messages.COMMAND_SENDER.message(locale));
    }
}
