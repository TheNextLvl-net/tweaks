package net.thenextlvl.tweaks.command.environment;

import net.thenextlvl.tweaks.command.api.WorldNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

abstract class WorldCommand implements TabExecutor {

    protected abstract void execute(World world);

    @Override
    public final boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) && args.length < 1)
            return false;

        World world;

        if (args.length == 0) {
            world = ((Player) sender).getWorld();
        } else if (args.length > 1) {
            return false;
        } else {
            world = Bukkit.getWorld(args[0]);
            if (world == null) {
                throw new WorldNotFoundException(args[0]);
            }
        }
        if (!isWorldAffected(world)) return false;
        execute(world);
        return true;
    }

    protected abstract boolean isWorldAffected(World world);

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 1) return Collections.emptyList();
        return Bukkit.getWorlds().stream().filter(this::isWorldAffected).map(World::getName)
                .filter(world -> world.toLowerCase().startsWith(args[args.length - 1].toLowerCase())).toList();
    }
}
