package net.thenextlvl.tweaks.command.api;

import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public abstract class CommandException extends RuntimeException {
    public final void handleException(CommandSender sender, Command command) {
        if (sender instanceof Player player) handle(player.locale(), player, command);
        else handle(Messages.ENGLISH, sender, command);
    }

    protected abstract void handle(Locale locale, CommandSender sender, Command command);
}
