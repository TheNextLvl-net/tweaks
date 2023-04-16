package net.thenextlvl.tweaks.command.environment;

import core.api.placeholder.Placeholder;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        var placeholder = Placeholder.<CommandSender>of("world", world.getName());
        var locale = sender instanceof Player player ? player.locale() : Messages.ENGLISH;
        sender.sendRichMessage(Messages.WEATHER_RAIN.message(locale, sender, placeholder));
    }

    @Override
    protected boolean isWorldAffected(World world) {
        return !world.hasCeiling();
    }
}
