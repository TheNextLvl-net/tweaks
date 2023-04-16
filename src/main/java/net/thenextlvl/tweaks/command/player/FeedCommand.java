package net.thenextlvl.tweaks.command.player;

import core.api.placeholder.Placeholder;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

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
        target.sendMessage(Messages.SATISFIED_HUNGER_SELF.message(target.locale()));
        if (target.equals(sender)) return;
        var placeholder = Placeholder.<CommandSender>of("player", target.getName());
        Locale locale = sender instanceof Player player ? player.locale() : Messages.ENGLISH;
        sender.sendMessage(Messages.SATISFIED_HUNGER_OTHERS.message(locale, placeholder));
    }
}
