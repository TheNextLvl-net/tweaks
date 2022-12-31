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

public class DeOPCommand extends TNLCommand {

    public DeOPCommand() {
        super("deop", "tnl.rights");
        setUsage("%prefix% §c/deop §8[§6Operator§8]");
    }

    @Override
    protected void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length < 1) throw new InvalidUseException(this);
        OfflinePlayer arg = Bukkit.getOfflinePlayer(args[0]);
        if (arg.getName() == null) throw new InvalidUseException(this);
        if (!arg.isOp()) throw new NothingChangedException();
        source.sendMessage(Messages.NO_LONGER_OP, new Placeholder("player", arg.getName()));
        arg.setOp(false);
    }

    @Override
    protected List<String> suggest(Invocation invocation) {
        List<String> suggestions = new ArrayList<>();
        if(invocation.arguments().length > 1) return suggestions;
        Bukkit.getOperators().forEach(all -> suggestions.add(all.getName()));
        return suggestions;
    }
}
