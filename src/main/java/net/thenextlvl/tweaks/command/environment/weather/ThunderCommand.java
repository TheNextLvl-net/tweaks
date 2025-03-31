package net.thenextlvl.tweaks.command.environment.weather;

import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.environment.WorldCommand;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class ThunderCommand extends WorldCommand {
    public ThunderCommand(TweaksPlugin plugin) {
        super(plugin);
    }

    public void register(Commands registrar) {
        var command = create(plugin.commands().thunder.command, "tweaks.command.weather.thunder");
        registrar.register(command, "Let it thunder", plugin.commands().thunder.aliases);
    }

    @Override
    protected void execute(CommandSender sender, World world) {
        plugin.bundle().sendMessage(sender, "command.weather.thunder",
                Placeholder.parsed("world", world.getName()));
        world.setThundering(true);
        world.setStorm(true);
    }

    @Override
    public boolean isWorldAffected(World world) {
        return !world.hasCeiling();
    }
}
