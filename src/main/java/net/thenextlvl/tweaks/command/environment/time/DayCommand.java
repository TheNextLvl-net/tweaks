package net.thenextlvl.tweaks.command.environment.time;

import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.environment.WorldCommand;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

@SuppressWarnings("UnstableApiUsage")
public class DayCommand extends WorldCommand {
    public DayCommand(TweaksPlugin plugin) {
        super(plugin);
    }

    public void register(Commands registrar) {
        var literal = create("day", "tweaks.command.day");
        registrar.register(literal, "Set the time to day");
    }

    @Override
    protected void execute(CommandSender sender, World world) {
        plugin.bundle().sendMessage(sender, "time.day", Placeholder.parsed("world", world.getName()));
        world.setTime(1000);
    }

    @Override
    public boolean isWorldAffected(World world) {
        return world.hasSkyLight();
    }
}