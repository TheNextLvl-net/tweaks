package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.listener.api.command.CommandSource;
import net.nonswag.tnl.listener.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class FlyCommand extends TNLCommand {

    public FlyCommand() {
        super("fly", "tnl.fly");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        TNLPlayer arg = null;
        if (args.length >= 1 && source.hasPermission("tnl.admin")) {
            arg = TNLPlayer.cast(args[0]);
            if (arg == null) source.sendMessage("%prefix% §4" + args[0] + "§c is not Online");
        } else if (source.isPlayer()) arg = source.player();
        else source.sendMessage("%prefix% §c/fly §8[§6Player§8]");
        if (arg != null) {
            boolean b = !arg.getAllowFlight();
            arg.setAllowFlight(b);
            arg.setFlying(b);
            if (source.isPlayer() && arg.equals(source)) source.sendMessage("%prefix% §7Flight§8: §6" + b);
            else source.sendMessage("%prefix% §7Flight §8(§a" + arg.getName() + "§8): §6" + b);
        }
    }

    @Nonnull
    @Override
    protected List<String> suggest(@Nonnull Invocation invocation) {
        String[] args = invocation.arguments();
        List<String> tabCompletions = new ArrayList<>();
        if (args.length <= 1 && invocation.source().hasPermission("tnl.admin")) {
            for (Player all : Bukkit.getOnlinePlayers()) tabCompletions.add(all.getName());
        }
        return tabCompletions;
    }
}
