package net.thenextlvl.tweaks.command.api;

import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.command.CommandSender;

public class NoBackLocationException extends CommandException {

    @Override
    public void handle(CommandSender sender) {
        TweaksPlugin.get().bundle().sendMessage(sender, "back.empty");
    }
}
