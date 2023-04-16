package net.thenextlvl.tweaks.command.environment;

import core.api.placeholder.Placeholder;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        var placeholder = Placeholder.<CommandSender>of("world", world.getName());
        var locale = sender instanceof Player player ? player.locale() : Messages.ENGLISH;
        sender.sendPlainMessage(Messages.TIME_NIGHT.message(locale, sender, placeholder));
    }

    @Override
    protected boolean isWorldAffected(World world) {
        return world.hasSkyLight();
    }
}
