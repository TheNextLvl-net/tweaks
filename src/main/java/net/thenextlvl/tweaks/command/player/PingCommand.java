package net.thenextlvl.tweaks.command.player;

import net.thenextlvl.tweaks.command.api.CommandInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@CommandInfo(name = "ping", usage = "/<command> (player)", permission = "tweaks.command.ping")
public class PingCommand implements CommandExecutor {
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
                // TODO: The player was not found
                return false;
            }
        }

        int ping = player.getPing();
        if (sender == player) {
            // TODO: Your ping is ...ms
        } else {
            // TODO: ...'s ping is ...ms
        }
        return true;
    }
}
