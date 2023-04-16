package net.thenextlvl.tweaks.command.server;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.config.BroadcastConfig;
import net.thenextlvl.tweaks.config.TweaksConfig;
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

        TweaksConfig tweaksConfig = plugin.getTweaksConfig();
        BroadcastConfig broadcastConfig = tweaksConfig.broadcastConfig();
        String format = broadcastConfig.format();
        String message = String.join(" ", args).replace("\\t", "   ");
        String replace = format(format, message);

        var miniMessage = MiniMessage.miniMessage();
        var deserialize = miniMessage.deserialize(replace);

        if (broadcastConfig.header() != null) {
            Bukkit.broadcast(miniMessage.deserialize(broadcastConfig.header()));
        }
        Bukkit.broadcast(deserialize);
        if (broadcastConfig.footer() != null) {
            Bukkit.broadcast(miniMessage.deserialize(broadcastConfig.footer()));
        }
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
