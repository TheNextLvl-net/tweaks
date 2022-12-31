package net.nonswag.tnl.tweaks.commands;

import net.nonswag.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import org.bukkit.Bukkit;

public class NightCommand extends TNLCommand {

    public NightCommand() {
        super("night", "tnl.time");
    }

    @Override
    protected void execute(Invocation invocation) {
        Bukkit.getWorlds().forEach(world -> world.setFullTime(13000));
        invocation.source().sendMessage("%prefix% §7Time§8: §6Night");
    }
}
