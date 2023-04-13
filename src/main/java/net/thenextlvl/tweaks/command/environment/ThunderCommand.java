package net.thenextlvl.tweaks.command.environment;

import net.thenextlvl.tweaks.api.command.CommandInfo;
import org.bukkit.World;

@CommandInfo(name = "thunder", permission = "tweaks.command.thunder", usage = "/thunder [<world>]")
public class ThunderCommand extends WorldCommand {

    @Override
    void execute(World world) {
        world.setStorm(true);
        world.setThundering(true);
    }
}
