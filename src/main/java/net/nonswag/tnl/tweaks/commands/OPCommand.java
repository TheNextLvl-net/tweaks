package net.nonswag.tnl.tweaks.commands;

import net.nonswag.core.api.command.CommandSource;
import net.nonswag.core.api.command.Invocation;
import net.nonswag.core.api.message.Placeholder;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.command.exceptions.InvalidUseException;
import net.nonswag.tnl.tweaks.commands.errors.NothingChangedException;
import net.nonswag.tnl.tweaks.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;

public class OPCommand extends TNLCommand {

    public OPCommand() {
        super("op", "tnl.rights");
        setUsage("%prefix% §c/op §8[§6Player§8]");
    }

    @Override
    protected void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length < 1) throw new InvalidUseException(this);
        if (!args[0].equalsIgnoreCase("list")) {
            OfflinePlayer arg = Bukkit.getOfflinePlayer(args[0]);
            if (arg.getName() == null) throw new InvalidUseException(this);
            if (arg.isOp()) throw new NothingChangedException();
            arg.setOp(true);
            source.sendMessage(Messages.NOW_OPERATOR, new Placeholder("player", arg.getName()));
        } else {
            List<String> s = new ArrayList<>();
            Bukkit.getOperators().forEach(all -> s.add(all.getName()));
            if (s.isEmpty()) source.sendMessage(Messages.NO_OPERATORS);
            else source.sendMessage("%prefix% §7Operators §8(§a" + s.size() + "§8): §6" + String.join("§8, §6", s));
        }
    }

    @Override
    protected List<String> suggest(Invocation invocation) {
        List<String> suggestions = new ArrayList<>();
        if (invocation.arguments().length > 1) return suggestions;
        for (OfflinePlayer all : Bukkit.getOfflinePlayers()) if (!all.isOp()) suggestions.add(all.getName());
        suggestions.add("list");
        return suggestions;
    }
}
