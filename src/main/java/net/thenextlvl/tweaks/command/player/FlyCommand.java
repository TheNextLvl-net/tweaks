package net.thenextlvl.tweaks.command.player;

import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.PlayerNotFoundException;
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

@CommandInfo(name = "fly", usage = "/<command> (player)", permission = "tweaks.command.fly")
public class FlyCommand implements TabExecutor {
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
                throw new PlayerNotFoundException(args[0]);
            }
        }
        player.setAllowFlight(!player.getAllowFlight());

        if (player.getAllowFlight()) {
            // TODO: Your flight mode was enabled.
            // TODO: You enabled the flight mode for
        } else {
            // TODO: Your flight mode was disabled.
            // TODO: You disabled the flight mode for ...
        }

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
