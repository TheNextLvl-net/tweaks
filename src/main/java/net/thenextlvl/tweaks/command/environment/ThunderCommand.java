package net.thenextlvl.tweaks.command.environment;

import core.api.placeholder.Placeholder;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        var placeholder = Placeholder.<CommandSender>of("world", world.getName());
        var locale = sender instanceof Player player ? player.locale() : Messages.ENGLISH;
        sender.sendPlainMessage(Messages.WEATHER_THUNDER.message(locale, sender, placeholder));
    }

    @Override
    protected boolean isWorldAffected(World world) {
        return !world.hasCeiling();
    }
}
