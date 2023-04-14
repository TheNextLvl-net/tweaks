package net.thenextlvl.tweaks.command.environment;

import net.thenextlvl.tweaks.command.api.CommandInfo;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

@CommandInfo(
        name = "thunder",
        permission = "tweaks.command.thunder",
        description = "let it thunder",
        usage = "/<command> (world)"
)
public class ThunderCommand extends WorldCommand {

    @Override
    protected void execute(CommandSender sender, World world) {
        world.setStorm(true);
        world.setThundering(true);
    }
}
