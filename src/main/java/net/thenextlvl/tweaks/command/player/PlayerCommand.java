package net.thenextlvl.tweaks.command.player;

import net.thenextlvl.tweaks.command.api.CommandException;
import net.thenextlvl.tweaks.command.api.OneOptionalArgumentCommand;
import net.thenextlvl.tweaks.command.api.PlayerNotOnlineException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

abstract class PlayerCommand extends OneOptionalArgumentCommand<Player> {

    @Override
    protected Player parse(Player player) {
        return player;
    }

    @Override
    protected Player parse(String argument) throws CommandException {
        Player player = Bukkit.getPlayer(argument);
        if (player == null) throw new PlayerNotOnlineException(argument);
        return player;
    }

    @Override
    protected @Nullable String getArgumentPermission(CommandSender sender, Player argument) {
        return null;
    }

    @Override
    protected Stream<String> suggest(CommandSender sender) {
        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> {
                    if (!isAllowed(sender, player)) return false;
                    var permission = getArgumentPermission(sender, player);
                    return permission == null || sender.hasPermission(permission);
                })
                .map(Player::getName);
    }
}
