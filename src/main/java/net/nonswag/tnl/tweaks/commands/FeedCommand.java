package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.core.api.command.CommandSource;
import net.nonswag.tnl.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class FeedCommand extends TNLCommand {

    public FeedCommand() {
        super("feed", "tnl.feed");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length >= 1) {
            TNLPlayer arg = TNLPlayer.cast(args[0]);
            if (arg != null) {
                arg.setFoodLevel(20);
                arg.setSaturation(20);
                source.sendMessage("%prefix% §6" + arg.getName() + "'s§a hunger has been satisfied");
            } else source.sendMessage("%prefix% §4" + args[0] + "§c is not Online");
        } else if (source.isPlayer()) {
            TNLPlayer player = (TNLPlayer) source.player();
            player.setFoodLevel(20);
            player.setSaturation(20);
            player.sendMessage("%prefix% §aYour hunger has been satisfied");
        } else source.sendMessage("%prefix% §c/feed §8[§6Player§8]");
    }

    @Nonnull
    @Override
    protected List<String> suggest(@Nonnull Invocation invocation) {
        String[] args = invocation.arguments();
        List<String> tabCompletions = new ArrayList<>();
        if (args.length <= 1) for (Player all : Bukkit.getOnlinePlayers()) tabCompletions.add(all.getName());
        return tabCompletions;
    }
}
