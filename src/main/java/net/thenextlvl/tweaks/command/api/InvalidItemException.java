package net.thenextlvl.tweaks.command.api;

import org.bukkit.command.CommandSender;

import java.util.Locale;

public class InvalidItemException extends CommandException {
    private final String arg;

    public InvalidItemException(String arg) {
        this.arg = arg;
    }

    @Override
    public void handle(Locale locale, CommandSender sender) {

    }
}
