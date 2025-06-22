package net.thenextlvl.tweaks.command.environment.weather;

import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.environment.WorldCommand;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class RainCommand extends WorldCommand {
    public RainCommand(TweaksPlugin plugin) {
        super(plugin);
    }

    public void register(Commands registrar) {
        var command = create(plugin.commands().rain.command, "tweaks.command.weather.rain");
        registrar.register(command, "Let it rain", plugin.commands().rain.aliases);
    }

    @Override
    protected void execute(CommandSender sender, World world) {
        if (Boolean.TRUE.equals(world.getGameRuleValue(GameRule.SEND_COMMAND_FEEDBACK)))
            plugin.bundle().sendMessage(sender, "command.weather.rain", Placeholder.parsed("world", world.getName()));
        world.setThundering(false);
        world.setStorm(true);
    }
}
