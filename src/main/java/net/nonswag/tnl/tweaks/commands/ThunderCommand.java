package net.nonswag.tnl.tweaks.commands;

import net.nonswag.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import org.bukkit.Bukkit;

public class ThunderCommand extends TNLCommand {

    public ThunderCommand() {
        super("thunder", "tnl.weather");
    }

    @Override
    protected void execute(Invocation invocation) {
        Bukkit.getWorlds().forEach(world -> {
            world.setThundering(true);
            world.setStorm(true);
        });
        invocation.source().sendMessage("%prefix% §7Weather§8: §6Thunder");
    }
}
