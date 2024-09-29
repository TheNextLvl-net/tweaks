package net.thenextlvl.tweaks.command.player;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class FeedCommand extends PlayerCommand {
    public FeedCommand(TweaksPlugin plugin) {
        super(plugin);
    }

    public void register(Commands registrar) {
        var command = create("feed", "tweaks.command.feed", "tweaks.command.feed.others");
        registrar.register(command, "Satisfy your own or someone else's hunger", List.of("flight"));
    }

    @Override
    protected int execute(CommandSender sender, Player player) {
        player.setExhaustion(0f);
        player.setFoodLevel(20);
        player.setSaturation(20f);

        player.playSound(player, Sound.ENTITY_PLAYER_BURP, SoundCategory.VOICE, 1f, 1f);
        plugin.bundle().sendMessage(player, "hunger.satisfied.self");
        if (player != sender) plugin.bundle().sendMessage(sender, "hunger.satisfied.others",
                Placeholder.parsed("player", player.getName()));

        return Command.SINGLE_SUCCESS;
    }
}
