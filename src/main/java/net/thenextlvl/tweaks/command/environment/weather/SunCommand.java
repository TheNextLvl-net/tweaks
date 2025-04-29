package net.thenextlvl.tweaks.command.environment.weather;

import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.environment.WorldCommand;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class SunCommand extends WorldCommand {
    public SunCommand(TweaksPlugin plugin) {
        super(plugin);
    }

    public void register(Commands registrar) {
        var command = create(plugin.commands().sun.command, "tweaks.command.weather.sun");
        registrar.register(command, "Let the sun shine", plugin.commands().sun.aliases);
    }

    @Override
    protected void execute(CommandSender sender, World world) {
        plugin.bundle().sendMessage(sender, "command.weather.sun",
                Placeholder.parsed("world", world.getName()));
        world.setThundering(false);
        world.setStorm(false);
    }
}
