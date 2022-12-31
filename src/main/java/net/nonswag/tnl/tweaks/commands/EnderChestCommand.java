package net.nonswag.tnl.tweaks.commands;

import net.nonswag.core.api.command.CommandSource;
import net.nonswag.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.command.exceptions.InvalidUseException;
import net.nonswag.tnl.listener.api.command.exceptions.PlayerNotOnlineException;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class EnderChestCommand extends TNLCommand {

    public EnderChestCommand() {
        super("enderchest", "tnl.enderchest", "ec");
        setUsage("%prefix% §c/enderchest §8[§6Player§8]");
    }

    @Override
    protected void execute(Invocation invocation) {
        TNLPlayer player = (TNLPlayer) invocation.source();
        String[] args = invocation.arguments();
        if (args.length < 1) throw new InvalidUseException(this);
        TNLPlayer arg = TNLPlayer.cast(args[0]);
        if (arg == null) throw new PlayerNotOnlineException(args[0]);
        player.inventoryManager().openInventory(arg.inventoryManager().getEnderChest());
    }

    @Override
    public boolean canUse(CommandSource source) {
        return source instanceof TNLPlayer;
    }

    @Override
    protected List<String> suggest(Invocation invocation) {
        List<String> suggestions = new ArrayList<>();
        if (invocation.arguments().length > 1) return suggestions;
        Bukkit.getOnlinePlayers().forEach(all -> suggestions.add(all.getName()));
        return suggestions;
    }
}
