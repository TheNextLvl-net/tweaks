package net.thenextlvl.tweaks.command.environment;

import net.thenextlvl.tweaks.command.api.CommandException;
import net.thenextlvl.tweaks.command.api.OneOptionalArgumentCommand;
import net.thenextlvl.tweaks.command.api.WorldNotAffectedException;
import net.thenextlvl.tweaks.command.api.WorldNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

abstract class WorldCommand extends OneOptionalArgumentCommand<World> {

    @Override
    protected World parse(Player player) {
        World world = player.getWorld();

        if (!isWorldAffected(world)) throw new WorldNotAffectedException(world);

        return world;
    }

    @Override
    protected World parse(String argument) throws CommandException {
        World world = Bukkit.getWorld(argument);

        if (world == null) throw new WorldNotFoundException(argument);
        if (!isWorldAffected(world)) throw new WorldNotAffectedException(world);

        return world;
    }

    @Override
    protected Stream<String> suggest(CommandSender sender) {
        return Bukkit.getWorlds().stream().filter(this::isWorldAffected).map(World::getName);
    }

    protected abstract boolean isWorldAffected(World world);

    @Override
    protected @Nullable String getArgumentPermission(CommandSender sender, World argument) {
        return null;
    }
}
