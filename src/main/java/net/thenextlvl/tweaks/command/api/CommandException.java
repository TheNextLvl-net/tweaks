package net.thenextlvl.tweaks.command.api;

import org.bukkit.command.CommandSender;

public abstract class CommandException extends RuntimeException {
    public abstract void handle(CommandSender sender);
}
