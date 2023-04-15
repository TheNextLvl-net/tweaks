package net.thenextlvl.tweaks.command.api;

import core.api.placeholder.Placeholder;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Locale;

@RequiredArgsConstructor
public class WorldNotFoundException extends CommandException {
    private final String input;

    @Override
    protected void handle(Locale locale, CommandSender sender, Command command) {
        var placeholder = Placeholder.<CommandSender>of("world", input);
        sender.sendPlainMessage(Messages.WORLD_NOT_FOUND.message(locale, sender, placeholder));
    }
}
