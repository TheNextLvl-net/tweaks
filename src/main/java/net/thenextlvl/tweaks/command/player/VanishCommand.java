package net.thenextlvl.tweaks.command.player;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class VanishCommand extends PlayerCommand {
    public VanishCommand(TweaksPlugin plugin) {
        super(plugin);
    }

    public void register(Commands registrar) {
        var command = create("vanish", "tweaks.command.vanish", "tweaks.command.vanish.others");
        registrar.register(command, "Hide yourself or someone else from others", List.of("v"));
    }

    @Override
    protected int execute(CommandSender sender, Player target) {
        target.setVisibleByDefault(!target.isVisibleByDefault());
        plugin.getServer().getOnlinePlayers().stream()
                .filter(player -> !target.equals(player))
                .forEach(player -> {
                    if (target.isVisibleByDefault()) player.showPlayer(plugin, target);
                    else player.hidePlayer(plugin, target);
                });

        var messageSelf = target.isVisibleByDefault() ? "command.vanish.disabled.self" : "command.vanish.enabled.self";
        var messageOthers = target.isVisibleByDefault() ? "command.vanish.disabled.others" : "command.vanish.enabled.others";
        plugin.bundle().sendMessage(target, messageSelf);
        if (target != sender) plugin.bundle().sendMessage(sender, messageOthers,
                Placeholder.parsed("player", target.getName()));
        return Command.SINGLE_SUCCESS;
    }
}
