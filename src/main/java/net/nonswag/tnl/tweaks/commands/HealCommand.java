package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.core.api.command.CommandSource;
import net.nonswag.tnl.core.api.command.Invocation;
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
                arg.bukkit().setHealth(20);
                arg.bukkit().setFoodLevel(20);
                arg.bukkit().setSaturation(20);
                arg.bukkit().setFireTicks(0);
                source.sendMessage("%prefix% §6" + arg.getName() + "§a got healed");
            } else source.sendMessage("%prefix% §4" + args[0] + "§c is not Online");
        } else if (source.isPlayer()) {
            TNLPlayer player = (TNLPlayer) source.player();
            player.bukkit().setHealth(20);
            player.bukkit().setFoodLevel(20);
            player.bukkit().setSaturation(20);
            player.bukkit().setFireTicks(0);
            player.messenger().sendMessage("%prefix% §aYou got healed");
        } else source.sendMessage("%prefix% §c/heal §8[§6Player§8]");
    }

    @Nonnull
    @Override
    protected List<String> suggest(@Nonnull Invocation invocation) {
        String[] args = invocation.arguments();
        List<String> suggestions = new ArrayList<>();
        if (args.length <= 1) for (Player all : Bukkit.getOnlinePlayers()) suggestions.add(all.getName());
        return suggestions;
    }
}
