package net.thenextlvl.tweaks.command.player;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(
        name = "vanish",
        description = "hide yourself or someone else from others",
        permission = "tweaks.command.vanish",
        aliases = {"v"}
)
@RequiredArgsConstructor
public class VanishCommand extends PlayerCommand {
    private final TweaksPlugin plugin;

    @Override
    protected void execute(CommandSender sender, Player target) {
        target.setVisibleByDefault(!target.isVisibleByDefault());
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> !target.equals(player))
                .forEach(player -> {
                    if (target.isVisibleByDefault()) player.showPlayer(plugin, target);
                    else player.hidePlayer(plugin, target);
                });

        var messageSelf = target.isVisibleByDefault() ? "vanish.disabled.self" : "vanish.enabled.self";
        var messageOthers = target.isVisibleByDefault() ? "vanish.disabled.others" : "vanish.enabled.others";
        plugin.bundle().sendMessage(target, messageSelf);
        if (target != sender) plugin.bundle().sendMessage(sender, messageOthers,
                Placeholder.parsed("player", target.getName()));
    }
}
