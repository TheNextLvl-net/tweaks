package net.thenextlvl.tweaks.command.environment;

import net.thenextlvl.tweaks.api.command.CommandInfo;
import org.bukkit.World;

@CommandInfo(name = "rain", permission = "tweaks.command.rain", usage = "/rain [<world>]")
public class RainCommand extends WorldCommand {

    @Override
    void execute(World world) {
        world.setStorm(true);
    }
}
