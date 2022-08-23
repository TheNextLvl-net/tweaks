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

public class DeOPCommand extends TNLCommand {

    public DeOPCommand() {
        super("deop", "tnl.rights");
        setUsage("%prefix% §c/deop §8[§6Operator§8]");
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length < 1) throw new InvalidUseException(this);
        OfflinePlayer arg = Bukkit.getOfflinePlayer(args[0]);
        if (arg.getName() == null) throw new InvalidUseException(this);
        if (arg.isOp()) {
            arg.setOp(false);
            source.sendMessage(Messages.NO_LONGER_OP, new Placeholder("player", arg.getName()));
        } else source.sendMessage(Messages.NOTHING_CHANGED);
    }

    @Nonnull
    @Override
    protected List<String> suggest(@Nonnull Invocation invocation) {
        List<String> suggestions = new ArrayList<>();
        if(invocation.arguments().length > 1) return suggestions;
        Bukkit.getOperators().forEach(all -> suggestions.add(all.getName()));
        return suggestions;
    }
}
