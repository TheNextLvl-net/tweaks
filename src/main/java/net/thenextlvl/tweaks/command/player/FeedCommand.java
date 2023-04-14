package net.thenextlvl.tweaks.command.player;

import net.thenextlvl.tweaks.command.api.CommandInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

@CommandInfo(name = "feed", usage = "/<command> (player)", permission = "tweaks.command.feed")
public class FeedCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player) && args.length < 1)
            return false;

        Player player;

        if (args.length == 0) {
            player = ((Player) sender);
        } else if (args.length > 1) {
            return false;
        } else {
            player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                // TODO: Send translated message
                return false;
            }
        }

        player.setFoodLevel(20);
        player.setSaturation(10f);
        player.setExhaustion(0f);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        }
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(s -> StringUtil.startsWithIgnoreCase(s, args[0])).toList();
        }
        return Collections.emptyList();
    }
}
