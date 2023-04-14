package net.thenextlvl.tweaks.command.environment;

import net.thenextlvl.tweaks.command.api.CommandException;
import net.thenextlvl.tweaks.command.api.OneOptionalArgumentCommand;
import net.thenextlvl.tweaks.command.api.WorldNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

abstract class WorldCommand extends OneOptionalArgumentCommand<World> {

    @Override
    protected World parse(Player player) {
        return player.getWorld();
    }

    @Override
    protected World parse(CommandSender sender, String argument) throws CommandException {
        World world = Bukkit.getWorld(argument);
        if (world == null) {
            throw new WorldNotFoundException(argument);
        }
        return world;
    }

    @Override
    protected Stream<String> tabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
