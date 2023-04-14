package net.thenextlvl.tweaks.command.player;

import net.thenextlvl.tweaks.command.api.CommandInfo;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "fly", usage = "/<command> (player)", permission = "tweaks.command.fly")
public class FlyCommand extends PlayerCommand {

    @Override
    protected void execute(CommandSender sender, Player value) {
        value.setAllowFlight(!value.getAllowFlight());

        if (value.getAllowFlight()) {
            // TODO: Your flight mode was enabled.
            // TODO: You enabled the flight mode for
        } else {
            // TODO: Your flight mode was disabled.
            // TODO: You disabled the flight mode for ...
        }

    }
}
