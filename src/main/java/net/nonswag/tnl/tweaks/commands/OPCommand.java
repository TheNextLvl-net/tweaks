package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.core.api.command.CommandSource;
import net.nonswag.tnl.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.command.exceptions.InvalidUseException;
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
        if (args.length >= 1) {
            if (!args[0].equalsIgnoreCase("list")) {
                OfflinePlayer arg = Bukkit.getOfflinePlayer(args[0]);
                if (arg.getName() != null) {
                    if (!arg.isOp()) {
                        arg.setOp(true);
                        source.sendMessage("%prefix% §6" + arg.getName() + "§a is now an operator");
                    } else source.sendMessage("%prefix% §cNothing could be changed");
                } else throw new InvalidUseException(this);
            } else {
                List<String> s = new ArrayList<>();
                for (OfflinePlayer player : Bukkit.getOperators()) s.add(player.getName());
                if (s.isEmpty()) source.sendMessage("%prefix% §cThere are no operators");
                else source.sendMessage("%prefix% §7Operators §8(§a" + s.size() + "§8): §6" + String.join("§8, §6", s));
            }
        } else throw new InvalidUseException(this);
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
