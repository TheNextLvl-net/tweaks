package net.thenextlvl.tweaks.command.environment;

import net.thenextlvl.tweaks.command.api.CommandInfo;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

@CommandInfo(
        name = "sun",
        permission = "tweaks.command.sun",
        description = "let the sun shine",
        usage = "/<command> (world)"
)
public class SunCommand extends WorldCommand {

    @Override
    protected void execute(CommandSender sender, World world) {
        world.setStorm(false);
        world.setThundering(false);
    }

    @Override
    protected boolean isWorldAffected(World world) {
        return !world.hasCeiling();
    }
}
