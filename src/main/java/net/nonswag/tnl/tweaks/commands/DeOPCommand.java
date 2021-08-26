package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.listener.api.command.CommandSource;
import net.nonswag.tnl.listener.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class DeOPCommand extends TNLCommand {

    public DeOPCommand() {
        super("deop", "tnl.rights");
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length >= 1) {
            OfflinePlayer arg = Bukkit.getOfflinePlayer(args[0]);
            if (arg.getName() != null && (arg.hasPlayedBefore() || arg.isOnline())) {
                if (arg.isOp()) {
                    arg.setOp(false);
                    source.sendMessage("%prefix% §6" + arg.getName() + "§a is no longer an operator");
                } else source.sendMessage("%prefix% §cNothing could be changed");
            } else source.sendMessage("%prefix% §c/deop §8[§6Operator§8]");
        } else source.sendMessage("%prefix% §c/deop §8[§6Operator§8]");
    }

    @Nonnull
    @Override
    protected List<String> suggest(@Nonnull Invocation invocation) {
        String[] args = invocation.arguments();
        List<String> suggestions = new ArrayList<>();
        if (args.length <= 1) {
            for (OfflinePlayer all : Bukkit.getOfflinePlayers()) if (all.isOp()) suggestions.add(all.getName());
        }
        return suggestions;
    }
}
