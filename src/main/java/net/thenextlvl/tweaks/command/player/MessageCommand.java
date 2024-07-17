package net.thenextlvl.tweaks.command.player;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@CommandInfo(
        name = "msg",
        aliases = {"tell", "w"},
        usage = "/msg [player] [message]",
        description = "start a conversation with another player",
        permission = "tweaks.command.msg"
)
@RequiredArgsConstructor
public class MessageCommand implements TabExecutor {
    private final TweaksPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            plugin.bundle().sendMessage(sender, "command.usage.msg");
            return true;
        }
        var player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            plugin.bundle().sendMessage(sender, "player.not.online", Placeholder.parsed("player", args[0]));
            return true;
        }
        if (player.equals(sender)) {
            plugin.bundle().sendMessage(sender, "message.self");
            return true;
        }
        message(plugin, Arrays.copyOfRange(args, 1, args.length), sender, player);
        return true;
    }

    static void message(TweaksPlugin plugin, String[] args, CommandSender source, CommandSender target) {
        var sender = Placeholder.parsed("sender", source.getName());
        var receiver = Placeholder.parsed("receiver", target.getName());
        var message = Placeholder.parsed("message", String.join(" ", args));
        plugin.bundle().sendMessage(source, "message.out", receiver, sender, message);
        plugin.bundle().sendMessage(target, "message.in", receiver, sender, message);
        plugin.conversations().put(target, source);
    }

    @Override
    public @Nullable List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length <= 1 ? Bukkit.getOnlinePlayers().stream()
                .filter(all -> !all.equals(sender))
                .map(Player::getName)
                .filter(s -> s.contains(args[args.length - 1]))
                .toList() : Collections.emptyList();
    }
}
