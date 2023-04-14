package net.thenextlvl.tweaks.command.api;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class PlayerNotFoundException extends CommandException {

    private final String input;

    public PlayerNotFoundException(String input) {
        this.input = input;
    }

    @Override
    public void handle(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage("§cYou are stupid");
    }
}
