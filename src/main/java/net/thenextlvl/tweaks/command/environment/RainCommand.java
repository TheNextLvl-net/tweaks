package net.thenextlvl.tweaks.command.environment;

import net.thenextlvl.tweaks.command.api.CommandInfo;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

@CommandInfo(
        name = "rain",
        permission = "tweaks.command.rain",
        description = "let it rain",
        usage = "/<command> (world)"
)
public class RainCommand extends WorldCommand {

    @Override
    protected void execute(CommandSender sender, World world) {
        world.setStorm(true);
        world.setThundering(false);
    }
}
