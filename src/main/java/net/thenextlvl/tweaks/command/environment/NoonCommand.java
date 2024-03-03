package net.thenextlvl.tweaks.command.environment;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

@CommandInfo(
        name = "noon",
        permission = "tweaks.command.noon",
        description = "set the time to noon",
        usage = "/<command> (world)"
)
@RequiredArgsConstructor
public class NoonCommand extends WorldCommand {

    private final TweaksPlugin plugin;

    @Override
    protected void execute(CommandSender sender, World world) {
        Bukkit.getGlobalRegionScheduler().run(plugin, task -> {
            world.setTime(6000);
            plugin.bundle().sendMessage(sender, "time.noon", Placeholder.parsed("world", world.getName()));
        });
    }

    @Override
    protected boolean isWorldAffected(World world) {
        return world.hasSkyLight();
    }
}
