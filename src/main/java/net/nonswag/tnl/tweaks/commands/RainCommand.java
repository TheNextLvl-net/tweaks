package net.nonswag.tnl.tweaks.commands;

import net.nonswag.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import org.bukkit.Bukkit;

public class RainCommand extends TNLCommand {

    public RainCommand() {
        super("rain", "tnl.weather");
    }

    @Override
    protected void execute(Invocation invocation) {
        Bukkit.getWorlds().forEach(world -> world.setStorm(true));
        invocation.source().sendMessage("%prefix% §7Weather§8: §6Rain");
    }
}



