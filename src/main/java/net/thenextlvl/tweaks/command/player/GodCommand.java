package net.thenextlvl.tweaks.command.player;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

@CommandInfo(
        name = "god",
        usage = "/<command> (player)",
        description = "make you or someone else invulnerable",
        permission = "tweaks.command.god"
)
@RequiredArgsConstructor
public class GodCommand extends PlayerCommand {
    private final TweaksPlugin plugin;

    @Override
    protected void execute(CommandSender sender, Player player) {
        player.setInvulnerable(!player.isInvulnerable());

        var messageSelf = player.isInvulnerable() ? "god.active.self" : "god.inactive.self";
        var messageOthers = player.isInvulnerable() ? "god.active.others" : "god.inactive.others";

        plugin.bundle().sendMessage(player, messageSelf);
        if (player != sender) plugin.bundle().sendMessage(sender, messageOthers,
                Placeholder.parsed("player", player.getName()));
    }

    @Override
    protected @Nullable String getArgumentPermission(CommandSender sender, Player argument) {
        return sender.equals(argument) ? null : "tweaks.command.god.others";
    }
}
