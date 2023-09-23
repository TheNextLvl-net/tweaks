package net.thenextlvl.tweaks.command.environment;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

@CommandInfo(
        name = "night",
        permission = "tweaks.command.night",
        description = "set the time to night",
        usage = "/<command> (world)"
)
@RequiredArgsConstructor
public class NightCommand extends WorldCommand {

    private final TweaksPlugin plugin;

    @Override
    protected void execute(CommandSender sender, World world) {
        Bukkit.getGlobalRegionScheduler().run(plugin, task -> {
            world.setTime(18000);
            plugin.bundle().sendMessage(sender, "time.night", Placeholder.parsed("world", world.getName()));
        });
    }

    @Override
    protected boolean isWorldAffected(World world) {
        return world.hasSkyLight();
    }
}
