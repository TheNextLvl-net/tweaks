package net.thenextlvl.tweaks.command.environment;

import net.thenextlvl.tweaks.command.api.CommandInfo;
import org.bukkit.World;

@CommandInfo(
        name = "rain",
        permission = "tweaks.command.rain",
        description = "let it rain",
        usage = "/<command> (world)"
)
public class RainCommand extends WorldCommand {

    @Override
    protected void execute(World world) {
        world.setStorm(true);
        world.setThundering(false);
    }

    @Override
    protected boolean isWorldAffected(World world) {
        return !world.hasCeiling();
    }
}
