package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.listener.api.command.CommandSource;
import net.nonswag.tnl.listener.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;

import javax.annotation.Nonnull;

public class DayCommand extends TNLCommand {

    public DayCommand() {
        super("day", "tnl.time");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        for (World world : Bukkit.getWorlds()) world.setTime(0);
        source.sendMessage("%prefix% §7Time§8: §6Day");
    }
}
