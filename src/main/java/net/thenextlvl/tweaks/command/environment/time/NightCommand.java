package net.thenextlvl.tweaks.command.environment.time;

import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.environment.WorldCommand;
import org.bukkit.GameRules;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class NightCommand extends WorldCommand {
    public NightCommand(final TweaksPlugin plugin) {
        super(plugin);
    }

    public void register(final Commands registrar) {
        final var command = create(plugin.commands().night.command, "tweaks.command.time.night");
        registrar.register(command, "Set the time to night", plugin.commands().night.aliases);
    }

    @Override
    protected void execute(final CommandSender sender, final World world) {
        if (Boolean.TRUE.equals(world.getGameRuleValue(GameRules.SEND_COMMAND_FEEDBACK)))
            plugin.bundle().sendMessage(sender, "command.time.night", Placeholder.parsed("world", world.getName()));
        world.setTime(13000);
    }
}
