package net.thenextlvl.tweaks.command.player;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

@CommandInfo(
        name = "fly",
        usage = "/<command> (player)",
        description = "toggle your own or someone else's fly state",
        permission = "tweaks.command.fly",
        aliases = {"flight"}
)
@RequiredArgsConstructor
public class FlyCommand extends PlayerCommand {
    private final TweaksPlugin plugin;

    @Override
    protected void execute(CommandSender sender, Player player) {
        player.setAllowFlight(!player.getAllowFlight());
        player.setFlying(player.getAllowFlight());

        var messageSelf = player.getAllowFlight() ? "flight.enabled.self" : "flight.disabled.self";
        var messageOthers = player.getAllowFlight() ? "flight.enabled.others" : "flight.disabled.others";

        plugin.bundle().sendMessage(player, messageSelf);
        if (player != sender) plugin.bundle().sendMessage(sender, messageOthers,
                Placeholder.component("player", player.name()));
    }

    @Override
    protected @Nullable String getArgumentPermission(CommandSender sender, Player argument) {
        return sender.equals(argument) ? null : "tweaks.command.fly.others";
    }
}
