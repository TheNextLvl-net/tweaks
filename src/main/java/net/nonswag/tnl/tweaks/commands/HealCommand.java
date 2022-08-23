package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.core.api.command.CommandSource;
import net.nonswag.tnl.core.api.command.Invocation;
import net.nonswag.tnl.core.api.message.Placeholder;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.command.exceptions.InsufficientPermissionException;
import net.nonswag.tnl.listener.api.command.exceptions.InvalidUseException;
import net.nonswag.tnl.listener.api.command.exceptions.PlayerNotOnlineException;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.tweaks.utils.Messages;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class HealCommand extends TNLCommand {

    public HealCommand() {
        super("heal", "tnl.heal");
        setUsage("%prefix% §c/heal §8[§6Player§8]");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length >= 1) {
            if (!invocation.source().hasPermission("tnl.admin")) throw new InsufficientPermissionException("tnl.admin");
            TNLPlayer arg = TNLPlayer.cast(args[0]);
            if (arg == null) throw new PlayerNotOnlineException(args[0]);
            arg.bukkit().setHealth(20);
            arg.bukkit().setFoodLevel(20);
            arg.bukkit().setSaturation(20);
            arg.bukkit().setFireTicks(0);
            source.sendMessage(Messages.HEALED_OTHER, new Placeholder("player", arg.getName()));
        } else if (source.isPlayer()) {
            TNLPlayer player = (TNLPlayer) source.player();
            player.bukkit().setHealth(20);
            player.bukkit().setFoodLevel(20);
            player.bukkit().setSaturation(20);
            player.bukkit().setFireTicks(0);
            player.messenger().sendMessage(Messages.HEALED_SELF);
        } else throw new InvalidUseException(this);
    }

    @Nonnull
    @Override
    protected List<String> suggest(@Nonnull Invocation invocation) {
        List<String> suggestions = new ArrayList<>();
        if (invocation.arguments().length > 1 || !invocation.source().hasPermission("tnl.admin")) return suggestions;
        Bukkit.getOnlinePlayers().forEach(all -> suggestions.add(all.getName()));
        return suggestions;
    }
}
