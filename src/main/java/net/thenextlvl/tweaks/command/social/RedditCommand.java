package net.thenextlvl.tweaks.command.social;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class RedditCommand {
    private final TweaksPlugin plugin;

    public RedditCommand(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().reddit.command).executes(context -> {
            plugin.bundle().sendMessage(context.getSource().getSender(), "social.reddit");
            return Command.SINGLE_SUCCESS;
        }).build();
        registrar.register(command, "Visit our subreddit", plugin.commands().reddit.aliases);
    }
}
