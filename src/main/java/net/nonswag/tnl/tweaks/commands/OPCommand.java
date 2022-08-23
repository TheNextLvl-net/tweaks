package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.core.api.command.CommandSource;
import net.nonswag.tnl.core.api.command.Invocation;
import net.nonswag.tnl.core.api.message.Placeholder;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.command.exceptions.InvalidUseException;
import net.nonswag.tnl.tweaks.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class OPCommand extends TNLCommand {

    public OPCommand() {
        super("op", "tnl.rights");
        setUsage("%prefix% §c/op §8[§6Player§8]");
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length < 1) throw new InvalidUseException(this);
        if (!args[0].equalsIgnoreCase("list")) {
            OfflinePlayer arg = Bukkit.getOfflinePlayer(args[0]);
            if (arg.getName() == null) throw new InvalidUseException(this);
            if (!arg.isOp()) {
                arg.setOp(true);
                source.sendMessage(Messages.NOW_OPERATOR, new Placeholder("player", arg.getName()));
            } else source.sendMessage(Messages.NOTHING_CHANGED);
        } else {
            List<String> s = new ArrayList<>();
            Bukkit.getOperators().forEach(all -> s.add(all.getName()));
            if (s.isEmpty()) source.sendMessage(Messages.NO_OPERATORS);
            else source.sendMessage("%prefix% §7Operators §8(§a" + s.size() + "§8): §6" + String.join("§8, §6", s));
        }
    }

    @Nonnull
    @Override
    protected List<String> suggest(@Nonnull Invocation invocation) {
        List<String> suggestions = new ArrayList<>();
        if (invocation.arguments().length > 1) return suggestions;
        for (OfflinePlayer all : Bukkit.getOfflinePlayers()) if (!all.isOp()) suggestions.add(all.getName());
        suggestions.add("list");
        return suggestions;
    }
}
