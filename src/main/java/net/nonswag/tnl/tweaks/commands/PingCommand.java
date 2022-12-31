package net.nonswag.tnl.tweaks.commands;

import net.nonswag.core.api.command.CommandSource;
import net.nonswag.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.command.exceptions.InsufficientPermissionException;
import net.nonswag.tnl.listener.api.command.exceptions.InvalidUseException;
import net.nonswag.tnl.listener.api.command.exceptions.PlayerNotOnlineException;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class PingCommand extends TNLCommand {

    public PingCommand() {
        super("ping");
        setUsage("%prefix% §c/ping §8[§6Player§8]");
    }

    @Override
    protected void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length >= 1) {
            if (!source.hasPermission("tnl.admin")) throw new InsufficientPermissionException("tnl.admin");
            TNLPlayer arg = TNLPlayer.cast(args[0]);
            if (arg == null) throw new PlayerNotOnlineException(args[0]);
            else source.sendMessage("%prefix% §7Ping §8(§a" + arg.getName() + "§8): §6" + arg.getPing() + "ms");
        } else {
            if (!(source instanceof TNLPlayer player)) throw new InvalidUseException(this);
            player.messenger().sendMessage("%prefix% §7Ping§8: §6" + player.getPing() + "ms");
        }
    }

    @Override
    protected List<String> suggest(Invocation invocation) {
        List<String> suggestions = new ArrayList<>();
        if (invocation.arguments().length > 1) return suggestions;
        Bukkit.getOnlinePlayers().forEach(all -> suggestions.add(all.getName()));
        return suggestions;
    }
}
