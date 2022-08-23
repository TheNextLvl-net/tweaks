package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.core.api.command.CommandSource;
import net.nonswag.tnl.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.command.exceptions.InsufficientPermissionException;
import net.nonswag.tnl.listener.api.command.exceptions.InvalidUseException;
import net.nonswag.tnl.listener.api.command.exceptions.PlayerNotOnlineException;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class FlyCommand extends TNLCommand {

    public FlyCommand() {
        super("fly", "tnl.fly");
        setUsage("%prefix% §c/fly §8[§6Player§8]");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        TNLPlayer arg;
        if (args.length >= 1) {
            if (!source.hasPermission("tnl.admin")) throw new InsufficientPermissionException("tnl.admin");
            if ((arg = TNLPlayer.cast(args[0])) == null) throw new PlayerNotOnlineException(args[0]);
        } else if (source.isPlayer()) arg = (TNLPlayer) invocation.source();
        else throw new InvalidUseException(this);
        boolean b = !arg.abilityManager().getAllowFlight();
        arg.abilityManager().setAllowFlight(b);
        arg.abilityManager().setFlying(b);
        if (source.isPlayer() && arg.equals(source)) source.sendMessage("%prefix% §7Flight§8: §6" + b);
        else source.sendMessage("%prefix% §7Flight §8(§a" + arg.getName() + "§8): §6" + b);
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
