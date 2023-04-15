package net.thenextlvl.tweaks.command.player;

import net.thenextlvl.tweaks.command.api.CommandException;
import net.thenextlvl.tweaks.command.api.OneOptionalArgumentCommand;
import net.thenextlvl.tweaks.command.api.PlayerNotOnlineException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.stream.Stream;

abstract class PlayerCommand extends OneOptionalArgumentCommand<Player> {

    @Override
    protected Player parse(Player player) {
        return player;
    }

    @Override
    protected Player parse(String argument) throws CommandException {
        Player player = Bukkit.getPlayer(argument);
        if (player == null) {
            throw new PlayerNotOnlineException(argument);
        }
        return player;
    }

    @Override
    protected Stream<String> suggest() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName);
    }
}
