package net.thenextlvl.tweaks.command.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
public class FlyCommand extends PlayerCommand {

    @Override
    protected void execute(CommandSender sender, Player player) {
        player.setAllowFlight(!player.getAllowFlight());
        player.setFlying(player.getAllowFlight());

        var messageSelf = "tweaks." + (player.getAllowFlight() ? "flight.enabled.self" : "flight.disabled.self");
        var messageOthers = "tweaks." + (player.getAllowFlight() ? "flight.enabled.others" : "flight.disabled.others");

        player.sendMessage(Component.translatable(messageSelf));
        if (player == sender) return;
        sender.sendRichMessage("<lang:" + messageOthers + ">", Placeholder.component("player", player.name()));
    }

    @Override
    protected @Nullable String getArgumentPermission(CommandSender sender, Player argument) {
        return sender.equals(argument) ? null : "tweaks.command.fly.others";
    }
}
