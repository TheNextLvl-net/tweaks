package net.thenextlvl.tweaks.command.environment.weather;

import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.environment.WorldCommand;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

@SuppressWarnings("UnstableApiUsage")
public class SunCommand extends WorldCommand {
    public SunCommand(TweaksPlugin plugin) {
        super(plugin);
    }

    public void register(Commands registrar) {
        var literal = create("sun", "tweaks.command.sun");
        registrar.register(literal, "Let the sun shine");
    }

    @Override
    protected void execute(CommandSender sender, World world) {
        plugin.bundle().sendMessage(sender, "weather.sun",
                Placeholder.parsed("world", world.getName()));
        world.setThundering(false);
        world.setStorm(false);
    }

    @Override
    public boolean isWorldAffected(World world) {
        return !world.hasCeiling();
    }
}
