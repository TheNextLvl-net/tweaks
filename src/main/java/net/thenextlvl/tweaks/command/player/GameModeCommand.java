package net.thenextlvl.tweaks.command.player;

import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.CommandSenderException;
import net.thenextlvl.tweaks.command.api.PlayerNotOnlineException;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

@CommandInfo(name = "gamemode", usage = "/<command> [game mode] (player)", aliases = "gm", permission = "tweaks.command.gamemode", description = "change your own or someone else's game mode")
public class GameModeCommand implements TabExecutor {

    public GameModeCommand() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) return false;

        GameMode mode;
        try {
            mode = GameMode.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            return false;
        }

        Player target;
        if (args.length == 2) target = Bukkit.getPlayer(args[1]);
        else if (sender instanceof Player player) target = player;
        else throw new CommandSenderException();

        if (target == null) throw new PlayerNotOnlineException(args[1]);


        target.setGameMode(mode);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length <= 1)
            return Arrays.stream(GameMode.values()).map(gameMode -> gameMode.name().toLowerCase()).toList();
        else if (args.length == 2) return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        return null;
    }
}
