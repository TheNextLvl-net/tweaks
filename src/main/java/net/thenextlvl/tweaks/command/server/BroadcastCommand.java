package net.thenextlvl.tweaks.command.server;

import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@CommandInfo(
        name = "broadcast",
        permission = "tweaks.command.broadcast",
        description = "broadcast a message",
        usage = "/<command> [message]",
        aliases = {"bc"}
)
@RequiredArgsConstructor
public class BroadcastCommand implements CommandExecutor {
    private final TweaksPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) return false;

        var tweaksConfig = plugin.config();
        var formattingConfig = tweaksConfig.formattingConfig();

        var format = formattingConfig.broadcastFormat();
        var message = String.join(" ", args).replace("\\t", "   ");

        if (formattingConfig.broadcastHeader() != null)
            Bukkit.broadcast(plugin.miniMessage().deserialize(formattingConfig.broadcastHeader()));

        Bukkit.broadcast(plugin.miniMessage().deserialize(format(format, message)));

        if (formattingConfig.broadcastFooter() != null)
            Bukkit.broadcast(plugin.miniMessage().deserialize(formattingConfig.broadcastFooter()));

        return true;
    }

    private String format(String format, String message) {
        String[] split = message.split("\\\\n");

        for (int i = 0; i < split.length; i++) {
            split[i] = format.replace("<message>", split[i]);
        }

        return String.join("\n", split);
    }
}
