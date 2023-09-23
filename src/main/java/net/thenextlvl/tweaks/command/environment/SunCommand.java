package net.thenextlvl.tweaks.command.environment;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

@CommandInfo(
        name = "sun",
        permission = "tweaks.command.sun",
        description = "let the sun shine",
        usage = "/<command> (world)"
)
@RequiredArgsConstructor
public class SunCommand extends WorldCommand {

    private final TweaksPlugin plugin;

    @Override
    protected void execute(CommandSender sender, World world) {
        Bukkit.getGlobalRegionScheduler().run(plugin, task -> {
            world.setStorm(false);
            world.setThundering(false);
            plugin.bundle().sendMessage(sender, "weather.sun", Placeholder.parsed("world", world.getName()));
        });
    }

    @Override
    protected boolean isWorldAffected(World world) {
        return !world.hasCeiling();
    }
}
