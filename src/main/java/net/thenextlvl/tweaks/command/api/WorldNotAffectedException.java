package net.thenextlvl.tweaks.command.api;

import core.api.placeholder.Placeholder;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.Locale;

@RequiredArgsConstructor
public class WorldNotAffectedException extends CommandException {
    private final World world;

    @Override
    protected void handle(Locale locale, CommandSender sender) {
        var placeholder = Placeholder.<CommandSender>of("world", world.getName());
        sender.sendPlainMessage(Messages.WORLD_NOT_AFFECTED.message(locale, sender, placeholder));
    }
}
