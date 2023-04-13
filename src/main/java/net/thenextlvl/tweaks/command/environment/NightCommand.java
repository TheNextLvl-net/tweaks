package net.thenextlvl.tweaks.command.environment;

import net.thenextlvl.tweaks.api.command.CommandInfo;
import org.bukkit.World;

@CommandInfo(name = "night", permission = "tweaks.command.night", usage = "/night [<world>]")
public class NightCommand extends WorldCommand {

    @Override
    void execute(World world) {
        world.setTime(13500);
    }
}
