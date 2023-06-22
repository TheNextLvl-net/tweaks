package net.thenextlvl.tweaks.command.player;

import core.api.placeholder.Placeholder;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.PlayerNotFoundException;
import net.thenextlvl.tweaks.util.Messages;
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
        var locale = sender instanceof Player player ? player.locale() : Messages.ENGLISH;
        if (lastSeenOnline(locale, sender, args)) return true;
        lastSeenOffline(locale, sender, args);
        return true;
    }

    private boolean lastSeenOnline(Locale locale, CommandSender sender, String[] args) {
        var target = Bukkit.getPlayer(args[0]);
        if (target == null) return false;
        var player = Placeholder.<CommandSender>of("player", target.getName());
        sender.sendRichMessage(Messages.LAST_SEEN_NOW.message(locale, sender, player));
        return true;
    }

    private void lastSeenOffline(Locale locale, CommandSender sender, String[] args) {
        plugin.getFoliaLib().getImpl()
                .runAsync(() -> {

                    var target = Bukkit.getOfflinePlayer(args[0]);
                    if (!target.hasPlayedBefore()) {
                        new PlayerNotFoundException(args[0]).handle(locale, sender);
                        return;
                    }

                    var lastSeen = new Date(target.getLastSeen());

                    var player = Placeholder.<CommandSender>of("player", () ->
                            target.getName() != null ? target.getName() : args[0]);
                    var time = Placeholder.<CommandSender>of("time", () -> {
                        var format = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
                        return format.format(lastSeen);
                    });

                    sender.sendRichMessage(Messages.LAST_SEEN_TIME.message(locale, sender, player, time));
                });
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).toList() : null;
    }
}
