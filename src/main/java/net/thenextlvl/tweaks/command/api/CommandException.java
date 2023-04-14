package net.thenextlvl.tweaks.command.api;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public abstract class CommandException extends RuntimeException {

    public abstract void handle(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args);

}
