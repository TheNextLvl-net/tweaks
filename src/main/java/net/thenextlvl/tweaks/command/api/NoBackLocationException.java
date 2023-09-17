package net.thenextlvl.tweaks.command.api;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public class NoBackLocationException extends CommandException {

    @Override
    public void handle(CommandSender sender) {
        sender.sendMessage(Component.translatable("tweaks.back.empty"));
    }
}
