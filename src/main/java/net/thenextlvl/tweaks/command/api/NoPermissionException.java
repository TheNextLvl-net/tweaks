package net.thenextlvl.tweaks.command.api;

import core.api.placeholder.Placeholder;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.command.CommandSender;

import java.util.Locale;

@RequiredArgsConstructor
public class NoPermissionException extends CommandException {
    private final String permission;

    @Override
    public void handle(Locale locale, CommandSender sender) {
        var placeholder = Placeholder.<CommandSender>of("permission", permission);
        sender.sendMessage(Messages.COMMAND_PERMISSION.message(locale, placeholder));
    }
}
