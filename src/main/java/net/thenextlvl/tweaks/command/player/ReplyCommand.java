package net.thenextlvl.tweaks.command.player;

import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@CommandInfo(
        name = "reply",
        aliases = "r",
        usage = "/reply [message]",
        description = "reply to the player you last chatted with",
        permission = "tweaks.command.reply"
)
@RequiredArgsConstructor
public class ReplyCommand implements CommandExecutor {
    private final TweaksPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            plugin.bundle().sendMessage(sender, "command.usage.reply");
            return true;
        }
        var player = plugin.conversations().get(sender);
        if (player == null) {
            plugin.bundle().sendMessage(sender, "conversation.running");
            return true;
        }
        MessageCommand.message(plugin, args, sender, player);
        return false;
    }
}
