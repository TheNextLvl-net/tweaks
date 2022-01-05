package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.core.api.command.CommandSource;
import net.nonswag.tnl.core.api.command.Invocation;
import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.player.TNLPlayer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class PingCommand extends TNLCommand {

    public PingCommand() {
        super("ping");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length >= 1 && source.hasPermission("tnl.admin")) {
            TNLPlayer arg = TNLPlayer.cast(args[0]);
            if (arg == null) source.sendMessage("%prefix%§4 " + args[0] + " §cis not Online");
            else source.sendMessage("%prefix% §7Ping §8(§a" + arg.getName() + "§8): §6" + arg.getPing() + "ms");
        } else {
            if (source.isPlayer()) {
                TNLPlayer player = (TNLPlayer) source.player();
                player.messenger().sendMessage("%prefix% §7Ping§8: §6" + player.getPing() + "ms");
            } else source.sendMessage("%prefix% §c/ping §8[§6Player§8]");
        }
    }

    @Nonnull
    @Override
    protected List<String> suggest(@Nonnull Invocation invocation) {
        String[] args = invocation.arguments();
        List<String> suggestions = new ArrayList<>();
        if (args.length <= 1) {
            for (TNLPlayer all : TNLListener.getOnlinePlayers()) {
                suggestions.add(all.getName());
            }
        }
        return suggestions;
    }
}
