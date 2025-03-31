package net.thenextlvl.tweaks.command.player;

import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class FeedCommand extends PlayersCommand {
    public FeedCommand(TweaksPlugin plugin) {
        super(plugin);
    }

    public void register(Commands registrar) {
        var command = create(plugin.commands().feed.command, "tweaks.command.feed", "tweaks.command.feed.others");
        registrar.register(command, "Satisfy your own or someone else's hunger", plugin.commands().feed.aliases);
    }

    @Override
    protected void execute(CommandSender sender, Player player) {
        player.setExhaustion(0f);
        player.setFoodLevel(20);
        player.setSaturation(20f);

        player.playSound(player, Sound.ENTITY_PLAYER_BURP, SoundCategory.VOICE, 1f, 1f);
        plugin.bundle().sendMessage(player, "command.hunger.satisfied.self");
        if (player != sender) plugin.bundle().sendMessage(sender, "command.hunger.satisfied.others",
                Placeholder.parsed("player", player.getName()));
    }
}
