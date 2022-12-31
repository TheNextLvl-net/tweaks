package net.nonswag.tnl.tweaks.commands;

import net.nonswag.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import org.bukkit.Bukkit;

public class DayCommand extends TNLCommand {

    public DayCommand() {
        super("day", "tnl.time");
    }

    @Override
    protected void execute(Invocation invocation) {
        Bukkit.getWorlds().forEach(world -> world.setFullTime(0));
        invocation.source().sendMessage("%prefix% §7Time§8: §6Day");
    }
}
