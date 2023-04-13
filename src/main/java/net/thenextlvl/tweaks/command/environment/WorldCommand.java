package net.thenextlvl.tweaks.command.environment;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

abstract class WorldCommand implements CommandExecutor {

    protected abstract void execute(World world);

    @Override
    public final boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
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
                // TODO: Send translated message
                return false;
            }
        }

        execute(world);
        return true;
    }

}
