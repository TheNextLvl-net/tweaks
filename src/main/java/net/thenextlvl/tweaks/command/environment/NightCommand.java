package net.thenextlvl.tweaks.command.environment;

import net.thenextlvl.tweaks.command.api.CommandInfo;
import org.bukkit.World;

@CommandInfo(
        name = "night",
        permission = "tweaks.command.night",
        description = "set the time to night",
        usage = "/<command> (world)"
)
public class NightCommand extends WorldCommand {

    @Override
    protected void execute(World world) {
        world.setTime(18000);
    }

    @Override
    protected boolean isWorldAffected(World world) {
        return world.hasSkyLight();
    }
}
