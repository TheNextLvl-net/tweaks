package net.thenextlvl.tweaks.command.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
public class FeedCommand extends PlayerCommand {

    @Override
    protected void execute(CommandSender sender, Player target) {
        notifySatisfaction(sender, target);
        target.setSaturation(10f);
        target.setExhaustion(0f);
        target.setFoodLevel(20);
    }

    private static void notifySatisfaction(CommandSender sender, Player target) {
        target.playSound(target, Sound.ENTITY_PLAYER_BURP, SoundCategory.VOICE, 1f, 1f);
        sender.sendMessage(Component.translatable("tweaks.hunger.satisfied.self"));
        if (target.equals(sender)) return;
        sender.sendRichMessage("<lang:tweaks.hunger.satisfied.others>", Placeholder.component("player", target.name()));
    }

    @Override
    protected @Nullable String getArgumentPermission(CommandSender sender, Player argument) {
        return sender.equals(argument) ? null : "tweaks.command.feed.others";
    }
}
