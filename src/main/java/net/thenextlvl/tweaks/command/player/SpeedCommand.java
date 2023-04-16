package net.thenextlvl.tweaks.command.player;

import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.CommandSenderException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.IntStream;

@CommandInfo(
        name = "speed",
        usage = "/<command> [speed]",
        permission = "tweaks.command.speed",
        description = "change your own or someone else's walk or fly speed"
)
public class SpeedCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player))
            throw new CommandSenderException();
        if (args.length < 1)
            return false;

        int speed;
        try {
            speed = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            return false;
        }

        if (speed < 0 || speed > 10) {
            return false;
        }

        if (player.isFlying())
            player.setFlySpeed(speed / 10f);
        else
            player.setWalkSpeed(speed / 10f);

        // TODO: Send message
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 1) return null;
        return IntStream.range(0, 11).mapToObj(Integer::toString).toList();
    }
}
