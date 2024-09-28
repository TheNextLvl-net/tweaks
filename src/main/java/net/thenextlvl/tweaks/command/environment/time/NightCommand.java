package net.thenextlvl.tweaks.command.environment.time;

import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.environment.WorldCommand;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

@SuppressWarnings("UnstableApiUsage")
public class NightCommand extends WorldCommand {
    public NightCommand(TweaksPlugin plugin) {
        super(plugin);
    }

    public void register(Commands registrar) {
        var literal = create("night", "tweaks.command.night");
        registrar.register(literal, "Set the time to night");
    }

    @Override
    protected void execute(CommandSender sender, World world) {
        plugin.bundle().sendMessage(sender, "time.night",
                Placeholder.parsed("world", world.getName()));
        world.setTime(13000);
    }

    @Override
    public boolean isWorldAffected(World world) {
        return world.hasSkyLight();
    }
}
