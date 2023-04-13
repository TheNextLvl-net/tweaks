package net.thenextlvl.tweaks.command.server;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.config.BroadcastConfig;
import net.thenextlvl.tweaks.config.TweaksConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 0)
            return false;

        TweaksConfig tweaksConfig = plugin.getTweaksConfig();
        BroadcastConfig broadcastConfig = tweaksConfig.broadcastConfig();
        String format = broadcastConfig.format();

        String broadcast = String.join(" ", args);

        var miniMessage = MiniMessage.miniMessage();
        var deserialize = miniMessage.deserialize(format, TagResolver.builder()
                .tag("message", (argumentQueue, context) -> Tag.inserting(miniMessage.deserialize(broadcast)))
                .build());
        Bukkit.broadcast(deserialize);
        return true;
    }
}
