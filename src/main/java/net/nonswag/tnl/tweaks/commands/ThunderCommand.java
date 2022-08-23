package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;

public class ThunderCommand extends TNLCommand {

    public ThunderCommand() {
        super("thunder", "tnl.weather");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        Bukkit.getWorlds().forEach(world -> {
            world.setThundering(true);
            world.setStorm(true);
        });
        invocation.source().sendMessage("%prefix% §7Weather§8: §6Thunder");
    }
}
