package net.nonswag.tnl.tweaks.commands;

import net.nonswag.core.api.command.CommandSource;
import net.nonswag.core.api.command.Invocation;
import net.nonswag.tnl.listener.Listener;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.command.exceptions.InvalidUseException;
import net.nonswag.tnl.listener.api.command.exceptions.PlayerNotOnlineException;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.tweaks.utils.Messages;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class InventoryCommand extends TNLCommand {

    public InventoryCommand() {
        super("inventory", "tnl.inventory", "inv");
        setUsage("%prefix% §c/inventory §8[§6Player§8]");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        TNLPlayer player = (TNLPlayer) source.player();
        if (args.length < 1) throw new InvalidUseException(this);
        TNLPlayer arg = TNLPlayer.cast(args[0]);
        if (arg == null) throw new PlayerNotOnlineException(args[0]);
        if (arg.equals(player)) player.messenger().sendMessage(Messages.SELECT_ANOTHER_PLAYER);
        else player.inventoryManager().openInventory(arg.inventoryManager().getInventory());
    }

    @Override
    public boolean canUse(@Nonnull CommandSource source) {
        return source.isPlayer();
    }

    @Nonnull
    @Override
    protected List<String> suggest(@Nonnull Invocation invocation) {
        List<String> suggestions = new ArrayList<>();
        CommandSource source = invocation.source();
        if (!source.isPlayer()) return suggestions;
        TNLPlayer player = (TNLPlayer) source.player();
        for (TNLPlayer all : Listener.getOnlinePlayers()) if (!all.equals(player)) suggestions.add(all.getName());
        return suggestions;
    }
}
