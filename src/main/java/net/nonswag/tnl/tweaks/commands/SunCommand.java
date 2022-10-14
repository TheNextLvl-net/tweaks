package net.nonswag.tnl.tweaks.commands;

import net.nonswag.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;

public class SunCommand extends TNLCommand {

    public SunCommand() {
        super("sun", "tnl.weather");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        Bukkit.getWorlds().forEach(world -> {
            world.setThundering(false);
            world.setStorm(false);
        });
        invocation.source().sendMessage("%prefix% §7Weather§8: §6Sun");
    }
}
