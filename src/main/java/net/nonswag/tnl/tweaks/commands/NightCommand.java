package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.core.api.command.CommandSource;
import net.nonswag.tnl.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;

import javax.annotation.Nonnull;

public class NightCommand extends TNLCommand {

    public NightCommand() {
        super("night", "tnl.time");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        for (World world : Bukkit.getWorlds()) world.setFullTime(13000);
        source.sendMessage("%prefix% §7Time§8: §6Night");
    }
}