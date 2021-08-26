package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.listener.api.command.CommandSource;
import net.nonswag.tnl.listener.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class HealCommand extends TNLCommand {

    public HealCommand() {
        super("heal", "tnl.heal");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length >= 1) {
            TNLPlayer arg = TNLPlayer.cast(args[0]);
            if (arg != null) {
                arg.setHealth(20);
                arg.setFoodLevel(20);
                arg.setSaturation(20);
                arg.setFireTicks(0);
                source.sendMessage("%prefix% §6" + arg.getName() + "§a got healed");
            } else source.sendMessage("%prefix% §4" + args[0] + "§c is not Online");
        } else if (source.isPlayer()) {
            source.player().setHealth(20);
            source.player().setFoodLevel(20);
            source.player().setSaturation(20);
            source.player().setFireTicks(0);
            source.player().sendMessage("%prefix% §aYou got healed");
        } else source.sendMessage("%prefix% §c/heal §8[§6Player§8]");
    }

    @Nonnull
    @Override
    protected List<String> suggest(@Nonnull Invocation invocation) {
        String[] args = invocation.arguments();
        List<String> tabCompletions = new ArrayList<>();
        if (args.length <= 1) for (Player all : Bukkit.getOnlinePlayers()) tabCompletions.add(all.getName());
        return tabCompletions;
    }
}
