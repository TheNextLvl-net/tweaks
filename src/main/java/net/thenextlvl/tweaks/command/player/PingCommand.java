package net.thenextlvl.tweaks.command.player;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

@CommandInfo(
        name = "ping",
        usage = "/<command> (player)",
        description = "see your own or someone else's latency",
        permission = "tweaks.command.ping",
        aliases = {"latency"}
)
@RequiredArgsConstructor
public class PingCommand extends PlayerCommand {
    private final TweaksPlugin plugin;

    @Override
    protected void execute(CommandSender sender, Player player) {
        if (sender != player) plugin.bundle().sendMessage(sender, "ping.others",
                Placeholder.parsed("player", player.getName()),
                Placeholder.parsed("ping", String.valueOf(player.getPing())));
        else plugin.bundle().sendMessage(player, "ping.self",
                Placeholder.parsed("ping", String.valueOf(player.getPing())));
    }

    @Override
    protected @Nullable String getArgumentPermission(CommandSender sender, Player argument) {
        return sender.equals(argument) ? null : "tweaks.command.ping.others";
    }
}
