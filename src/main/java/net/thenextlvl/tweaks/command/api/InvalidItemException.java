package net.thenextlvl.tweaks.command.api;

import core.api.placeholder.Placeholder;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.command.CommandSender;

import java.util.Locale;

@RequiredArgsConstructor
public class InvalidItemException extends CommandException {
    private final String argument;

    @Override
    public void handle(Locale locale, CommandSender sender) {
        sender.sendRichMessage(Messages.INVALID_ITEM.message(locale, sender, Placeholder.of("item", argument)));
    }
}
