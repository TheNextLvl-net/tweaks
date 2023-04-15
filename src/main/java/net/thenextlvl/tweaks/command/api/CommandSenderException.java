package net.thenextlvl.tweaks.command.api;

import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Locale;

public class CommandSenderException extends CommandException {
    @Override
    protected void handle(Locale locale, CommandSender sender, Command command) {
        sender.sendPlainMessage(Messages.COMMAND_SENDER.message(locale));
    }
}
