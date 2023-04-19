package net.thenextlvl.tweaks.command.player;

import core.api.placeholder.Placeholder;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.NoPermissionException;
import net.thenextlvl.tweaks.command.api.PlayerNotOnlineException;
import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.IntStream;

@CommandInfo(
        name = "speed",
        usage = "/<command> [speed] (player)",
        permission = "tweaks.command.speed",
        description = "change your own or someone else's walk or fly speed"
)
public class SpeedCommand implements TabExecutor {

    private static final String OTHERS_PERMISSION = "tweaks.command.speed.others";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1 || args.length > 2)
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

        Player target = args.length == 2 ? Bukkit.getPlayer(args[1]) : sender instanceof Player player ? player : null;

        if (target != sender && !sender.hasPermission(OTHERS_PERMISSION))
            throw new NoPermissionException(OTHERS_PERMISSION);

        if (target == null && args.length == 2) throw new PlayerNotOnlineException(args[1]);
        if (target == null) return false;

        if (target.isFlying())
            target.setFlySpeed(speed / 10f);
        else
            target.setWalkSpeed(speed / 10f);

        var messageSelf = target.isFlying() ? Messages.FLY_SPEED_CHANGED_SELF : Messages.WALK_SPEED_CHANGED_SELF;
        var messageOthers = target.isFlying() ? Messages.FLY_SPEED_CHANGED_OTHERS : Messages.WALK_SPEED_CHANGED_OTHERS;

        var placeholder1 = Placeholder.<CommandSender>of("speed", speed);
        target.sendRichMessage(messageSelf.message(target.locale(), target, placeholder1));
        if (target == sender) return true;
        var locale = sender instanceof Player player ? player.locale() : Messages.ENGLISH;
        var placeholder2 = Placeholder.<CommandSender>of("player", target.getName());
        sender.sendRichMessage(messageOthers.message(locale, sender, placeholder1, placeholder2));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 2) return null;
        if (args.length == 2 && sender.hasPermission(OTHERS_PERMISSION))
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        return IntStream.range(0, 11).mapToObj(Integer::toString).toList();
    }
}
