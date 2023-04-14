package net.thenextlvl.tweaks.command.api;

import lombok.RequiredArgsConstructor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class WorldNotAffectedException extends CommandException {

    private final World world;

    @Override
    public void handle(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage("§cYou are stupid");
    }
}
