package net.thenextlvl.tweaks.command.environment;

import net.thenextlvl.tweaks.command.api.CommandInfo;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

@CommandInfo(
        name = "night",
        permission = "tweaks.command.night",
        description = "set the time to night",
        usage = "/<command> (world)"
)
public class NightCommand extends WorldCommand {

    @Override
    protected void execute(CommandSender sender, World world) {
        world.setTime(18000);
    }
}
