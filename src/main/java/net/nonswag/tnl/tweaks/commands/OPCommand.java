package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.core.api.command.CommandSource;
import net.nonswag.tnl.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class OPCommand extends TNLCommand {

    public OPCommand() {
        super("op", "tnl.rights");
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length >= 1) {
            OfflinePlayer arg = Bukkit.getOfflinePlayer(args[0]);
            if (arg.getName() != null && (arg.hasPlayedBefore() || arg.isOnline())) {
                if (!arg.isOp()) {
                    arg.setOp(true);
                    source.sendMessage("%prefix% §6" + arg.getName() + "§a is now an operator");
                } else source.sendMessage("%prefix% §cNothing could be changed");
            } else source.sendMessage("%prefix% §c/op §8[§6Player§8]");
        } else source.sendMessage("%prefix% §c/op §8[§6Player§8]");
    }

    @Nonnull
    @Override
    protected List<String> suggest(@Nonnull Invocation invocation) {
        String[] args = invocation.arguments();
        List<String> suggestions = new ArrayList<>();
        if (args.length <= 1) {
            for (OfflinePlayer all : Bukkit.getOfflinePlayers()) if (!all.isOp()) suggestions.add(all.getName());
        }
        return suggestions;
    }
}
