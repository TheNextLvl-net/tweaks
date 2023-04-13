package net.thenextlvl.tweaks.command.environment;

import net.thenextlvl.tweaks.api.command.CommandInfo;
import org.bukkit.World;

@CommandInfo(name = "day", permission = "tweaks.command.day", usage = "/day [<world>]")
public class DayCommand extends WorldCommand {

    @Override
    void execute(World world) {
        world.setTime(0);
    }
}
