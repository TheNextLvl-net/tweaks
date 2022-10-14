package net.nonswag.tnl.tweaks.commands;

import net.nonswag.core.api.command.CommandSource;
import net.nonswag.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.command.exceptions.InvalidUseException;
import net.nonswag.tnl.listener.api.command.exceptions.PlayerNotOnlineException;
import net.nonswag.tnl.listener.api.gamemode.Gamemode;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.tweaks.utils.Messages;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GamemodeCommand extends TNLCommand {

    public GamemodeCommand() {
        super("gamemode", "tnl.gamemode", "gm");
        setUsage("%prefix% §c/gamemode §8[§6Mode§8] §8[§6Player§8]");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length < 1) throw new InvalidUseException(this);
        Gamemode gamemode = Gamemode.cast(args[0]);
        if (gamemode == null) throw new InvalidUseException(this);
        TNLPlayer player = null;
        if (args.length >= 2) {
            if ((player = TNLPlayer.cast(args[1])) == null) throw new PlayerNotOnlineException(args[1]);
        } else if (source.isPlayer()) player = (TNLPlayer) source.player();
        else source.sendMessage("%prefix% §c/gamemode " + gamemode.getName() + " §8[§6Player§8]");
        if (player == null) return;
        if (!player.getGamemode().equals(gamemode)) {
            player.setGamemode(gamemode);
            if (source.equals(player)) source.sendMessage("%prefix% §7Gamemode§8: §6" + gamemode.getName());
            else source.sendMessage("%prefix% §7Gamemode §8(§a" + player.getName() + "§8): §6" + gamemode.getName());
        } else source.sendMessage(Messages.NOTHING_CHANGED);
    }

    @Nonnull
    @Override
    protected List<String> suggest(@Nonnull Invocation invocation) {
        String[] args = invocation.arguments();
        List<String> suggestions = new ArrayList<>();
        if (args.length <= 1) for (Gamemode mode : Gamemode.values()) suggestions.add(mode.getName());
        else if (args.length == 2) Bukkit.getOnlinePlayers().forEach(all -> suggestions.add(all.getName()));
        return suggestions;
    }
}
