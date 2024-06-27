package net.thenextlvl.tweaks.command.player;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

@CommandInfo(
        name = "feed",
        usage = "/<command> (player)",
        description = "satisfy your own or someone else's hunger",
        permission = "tweaks.command.feed"
)
@RequiredArgsConstructor
public class FeedCommand extends PlayerCommand {
    private final TweaksPlugin plugin;

    @Override
    protected void execute(CommandSender sender, Player target) {
        notifySatisfaction(sender, target);
        target.setSaturation(10f);
        target.setExhaustion(0f);
        target.setFoodLevel(20);
    }

    private void notifySatisfaction(CommandSender sender, Player player) {
        player.playSound(player, Sound.ENTITY_PLAYER_BURP, SoundCategory.VOICE, 1f, 1f);
        plugin.bundle().sendMessage(player, "hunger.satisfied.self");
        if (player != sender) plugin.bundle().sendMessage(sender, "hunger.satisfied.others",
                Placeholder.parsed("player", player.getName()));
    }

    @Override
    protected @Nullable String getArgumentPermission(CommandSender sender, Player argument) {
        return sender.equals(argument) ? null : "tweaks.command.feed.others";
    }
}
