package net.nonswag.tnl.tweaks.commands;

import net.nonswag.core.api.command.CommandSource;
import net.nonswag.core.api.command.Invocation;
import net.nonswag.core.api.message.Placeholder;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.command.exceptions.InvalidUseException;
import net.nonswag.tnl.listener.api.command.exceptions.PlayerNotOnlineException;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.tweaks.utils.Messages;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ClearCommand extends TNLCommand {

    public ClearCommand() {
        super("clear", "tnl.clear");
        setUsage("%prefix% §c/clear §8[§6Player§8]");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length >= 1) {
            TNLPlayer arg = TNLPlayer.cast(args[0]);
            if (arg == null) throw new PlayerNotOnlineException(args[0]);
            arg.inventoryManager().getInventory().clear();
            source.sendMessage(Messages.CLEARED_INVENTORY, new Placeholder("player", arg.getName()));
        } else if (source.isPlayer()) {
            TNLPlayer player = (TNLPlayer) source.player();
            player.inventoryManager().getInventory().clear();
            player.messenger().sendMessage(Messages.CLEARED_OWN_INVENTORY);
        } else throw new InvalidUseException(this);
    }

    @Nonnull
    @Override
    protected List<String> suggest(@Nonnull Invocation invocation) {
        List<String> suggestions = new ArrayList<>();
        if (invocation.arguments().length > 1) return suggestions;
        Bukkit.getOnlinePlayers().forEach(all -> suggestions.add(all.getName()));
        return suggestions;
    }
}
