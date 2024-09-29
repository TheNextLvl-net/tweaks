package net.thenextlvl.tweaks.command.environment.time;

import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.environment.WorldCommand;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

@SuppressWarnings("UnstableApiUsage")
public class MidnightCommand extends WorldCommand {
    public MidnightCommand(TweaksPlugin plugin) {
        super(plugin);
    }

    public void register(Commands registrar) {
        var command = create("midnight", "tweaks.command.midnight");
        registrar.register(command, "Set the time to midnight");
    }

    @Override
    protected void execute(CommandSender sender, World world) {
        plugin.bundle().sendMessage(sender, "time.midnight",
                Placeholder.parsed("world", world.getName()));
        world.setTime(18000);
    }

    @Override
    public boolean isWorldAffected(World world) {
        return world.hasSkyLight();
    }
}
