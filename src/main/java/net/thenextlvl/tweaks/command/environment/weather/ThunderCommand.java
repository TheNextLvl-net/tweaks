package net.thenextlvl.tweaks.command.environment.weather;

import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.environment.WorldCommand;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

@SuppressWarnings("UnstableApiUsage")
public class ThunderCommand extends WorldCommand {
    public ThunderCommand(TweaksPlugin plugin) {
        super(plugin);
    }

    public void register(Commands registrar) {
        var command = create("thunder", "tweaks.command.thunder");
        registrar.register(command, "Let it thunder");
    }

    @Override
    protected void execute(CommandSender sender, World world) {
        plugin.bundle().sendMessage(sender, "weather.thunder",
                Placeholder.parsed("world", world.getName()));
        world.setThundering(true);
        world.setStorm(true);
    }

    @Override
    public boolean isWorldAffected(World world) {
        return !world.hasCeiling();
    }
}
