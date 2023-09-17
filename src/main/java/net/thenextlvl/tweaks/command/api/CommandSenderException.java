package net.thenextlvl.tweaks.command.api;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

import java.util.Locale;

public class CommandSenderException extends CommandException {
    @Override
    public void handle(Locale locale, CommandSender sender) {
        sender.sendMessage(Component.translatable("tweaks.command.sender"));
    }
}
