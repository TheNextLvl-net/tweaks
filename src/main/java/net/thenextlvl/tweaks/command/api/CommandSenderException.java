package net.thenextlvl.tweaks.command.api;

import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.command.CommandSender;

public class CommandSenderException extends CommandException {
    @Override
    public void handle(CommandSender sender) {
        TweaksPlugin.get().bundle().sendMessage(sender, "command.sender");
    }
}
