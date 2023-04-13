package net.thenextlvl.tweaks.command.environment;

import net.thenextlvl.tweaks.api.command.CommandInfo;
import org.bukkit.World;

@CommandInfo(name = "sun", permission = "tweaks.command.sun", usage = "/sun [<world>]")
public class SunCommand extends WorldCommand {

    @Override
    void execute(World world) {
        world.setStorm(false);
        world.setThundering(false);
    }
}
