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
public class NoonCommand extends WorldCommand {
    public NoonCommand(TweaksPlugin plugin) {
        super(plugin);
    }

    public void register(Commands registrar) {
        var command = create(plugin.commands().noon.command, "tweaks.command.time.noon");
        registrar.register(command, "Set the time to noon", plugin.commands().noon.aliases);
    }

    @Override
    protected void execute(CommandSender sender, World world) {
        if (Boolean.TRUE.equals(world.getGameRuleValue(GameRules.SEND_COMMAND_FEEDBACK)))
            plugin.bundle().sendMessage(sender, "command.time.noon", Placeholder.parsed("world", world.getName()));
        world.setTime(6000);
    }
}
