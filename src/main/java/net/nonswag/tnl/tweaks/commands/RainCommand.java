package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.core.api.command.CommandSource;
import net.nonswag.tnl.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;

import javax.annotation.Nonnull;

public class RainCommand extends TNLCommand {

    public RainCommand() {
        super("rain", "tnl.weather");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        for (World world : Bukkit.getWorlds()) world.setStorm(true);
        source.sendMessage("%prefix% §7Weather§8: §6Rain");
    }
}


