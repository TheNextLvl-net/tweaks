package net.thenextlvl.tweaks.command.environment.weather;

import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.environment.WorldCommand;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

@SuppressWarnings("UnstableApiUsage")
public class RainCommand extends WorldCommand {
    public RainCommand(TweaksPlugin plugin) {
        super(plugin);
    }

    public void register(Commands registrar) {
        var command = create("rain", "tweaks.command.rain");
        registrar.register(command, "Let it rain");
    }

    @Override
    protected void execute(CommandSender sender, World world) {
        plugin.bundle().sendMessage(sender, "command.weather.rain",
                Placeholder.parsed("world", world.getName()));
        world.setThundering(false);
        world.setStorm(true);
    }

    @Override
    public boolean isWorldAffected(World world) {
        return !world.hasCeiling();
    }
}
