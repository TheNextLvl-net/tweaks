package net.thenextlvl.tweaks.command.environment;

import net.thenextlvl.tweaks.command.api.CommandInfo;
import org.bukkit.World;

@CommandInfo(
        name = "day",
        permission = "tweaks.command.day",
        description = "set the time to day",
        usage = "/<command> (world)"
)
public class DayCommand extends WorldCommand {

    @Override
    protected void execute(World world) {
        world.setTime(1000);
    }

    @Override
    protected boolean isWorldAffected(World world) {
        return world.hasSkyLight();
    }
}
