package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.listener.api.command.CommandSource;
import net.nonswag.tnl.listener.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;

import javax.annotation.Nonnull;

public class SunCommand extends TNLCommand {

    public SunCommand() {
        super("sun", "tnl.weather");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        if (source.isPlayer()) {
            source.player().getWorld().setThundering(false);
            source.player().getWorld().setStorm(false);
        } else for (World world : Bukkit.getWorlds()) {
            world.setThundering(false);
            world.setStorm(false);
        }
        source.sendMessage("%prefix% §7Weather§8: §6Sun");
    }
}
