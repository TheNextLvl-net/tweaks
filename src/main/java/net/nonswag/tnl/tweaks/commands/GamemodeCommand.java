package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.core.api.command.CommandSource;
import net.nonswag.tnl.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.gamemode.Gamemode;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GamemodeCommand extends TNLCommand {

    public GamemodeCommand() {
        super("gamemode", "tnl.gamemode", "gm");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length >= 1) {
            Gamemode gamemode = Gamemode.cast(args[0]);
            if (gamemode.isUnknown()) source.sendMessage("%prefix% §c/gamemode §8[§6Mode§8] §8[§6Player§8]");
            else {
                TNLPlayer player = null;
                if (args.length >= 2) {
                    player = TNLPlayer.cast(args[1]);
                    if (player == null) source.sendMessage("%prefix% §4" + args[1] + "§c is not Online");
                } else if (source.isPlayer()) player = (TNLPlayer) source.player();
                else source.sendMessage("%prefix% §c/gamemode " + gamemode.getName() + " §8[§6Player§8]");
                if (player != null) {
                    if (player.getGamemode().equals(gamemode)) {
                        source.sendMessage("%prefix% §cNothing could be changed");
                    } else {
                        player.setGamemode(gamemode);
                        if (source.equals(player)) {
                            source.sendMessage("%prefix% §7Gamemode§8: §6" + gamemode.getName());
                        } else {
                            source.sendMessage("%prefix% §7Gamemode §8(§a" + player.getName() + "§8): §6" + gamemode.getName());
                        }
                    }
                }
            }
        } else source.sendMessage("%prefix% §c/gamemode §8[§6Mode§8] §8[§6Player§8]");
    }

    @Nonnull
    @Override
    protected List<String> suggest(@Nonnull Invocation invocation) {
        String[] args = invocation.arguments();
        List<String> tabCompletions = new ArrayList<>();
        if (args.length <= 1) for (Gamemode mode : Gamemode.values()) {
            if (!mode.isUnknown()) tabCompletions.add(mode.getName());
        }
        else if (args.length == 2) for (Player all : Bukkit.getOnlinePlayers()) tabCompletions.add(all.getName());
        return tabCompletions;
    }
}
