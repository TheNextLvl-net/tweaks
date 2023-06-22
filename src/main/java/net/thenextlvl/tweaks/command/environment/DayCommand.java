package net.thenextlvl.tweaks.command.environment;

import core.api.placeholder.Placeholder;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "day", permission = "tweaks.command.day", description = "set the time to day", usage = "/<command> (world)")
@RequiredArgsConstructor
public class DayCommand extends WorldCommand {

    private final TweaksPlugin plugin;

    @Override
    protected void execute(CommandSender sender, World world) {
        plugin.getFoliaLib().getImpl().runNextTick(() -> {
            world.setTime(1000);
            var placeholder = Placeholder.<CommandSender>of("world", world.getName());
            var locale = sender instanceof Player player ? player.locale() : Messages.ENGLISH;
            sender.sendRichMessage(Messages.TIME_DAY.message(locale, sender, placeholder));
        });
    }

    @Override
    protected boolean isWorldAffected(World world) {
        return world.hasSkyLight();
    }
}
