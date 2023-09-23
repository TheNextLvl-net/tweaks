package net.thenextlvl.tweaks.command.player;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.PlayerNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@CommandInfo(
        name = "seen",
        description = "gives you information about a player",
        permission = "tweaks.command.seen",
        usage = "/<command> [player]",
        aliases = {"find"}
)
@RequiredArgsConstructor
public class SeenCommand implements TabExecutor {
    private final TweaksPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) return false;
        if (lastSeenOnline(sender, args)) return true;
        lastSeenOffline(sender, args);
        return true;
    }

    private boolean lastSeenOnline(CommandSender sender, String[] args) {
        var target = Bukkit.getPlayer(args[0]);
        if (target == null) return false;
        plugin.bundle().sendMessage(sender, "last.seen.now", Placeholder.component("player", target.name()));
        return true;
    }

    private void lastSeenOffline(CommandSender sender, String[] args) {
        Bukkit.getAsyncScheduler().runNow(plugin, task -> {

            var target = Bukkit.getOfflinePlayer(args[0]);
            if (!target.hasPlayedBefore()) throw new PlayerNotFoundException(args[0]);

            var lastSeen = new Date(target.getLastSeen());
            var locale = sender instanceof Player player ? player.locale() : Locale.US;
            var format = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

            plugin.bundle().sendMessage(sender, "last.seen.time",
                    Placeholder.parsed("player", target.getName() != null ? target.getName() : args[0]),
                    Placeholder.parsed("time", format.format(lastSeen)));
        });
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).toList() : null;
    }
}
