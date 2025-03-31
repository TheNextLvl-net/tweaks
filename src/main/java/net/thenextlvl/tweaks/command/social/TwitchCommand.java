package net.thenextlvl.tweaks.command.social;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class TwitchCommand {
    private final TweaksPlugin plugin;

    public TwitchCommand(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().twitch.command).executes(context -> {
            plugin.bundle().sendMessage(context.getSource().getSender(), "social.twitch");
            return Command.SINGLE_SUCCESS;
        }).build();
        registrar.register(command, "Follow us on Twitch", plugin.commands().twitch.aliases);
    }
}
