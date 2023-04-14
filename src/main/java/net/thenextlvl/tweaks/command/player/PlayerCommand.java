package net.thenextlvl.tweaks.command.player;

import net.thenextlvl.tweaks.command.api.CommandException;
import net.thenextlvl.tweaks.command.api.OneOptionalArgumentCommand;
import net.thenextlvl.tweaks.command.api.PlayerNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

abstract class PlayerCommand extends OneOptionalArgumentCommand<Player> {

    @Override
    protected Player parse(Player player) {
        return player;
    }

    @Override
    protected Player parse(CommandSender sender, String argument) throws CommandException {
        Player player = Bukkit.getPlayer(argument);
        if (player == null) {
            throw new PlayerNotFoundException(argument);
        }
        return player;
    }

    protected Stream<String> tabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName);
    }

}
