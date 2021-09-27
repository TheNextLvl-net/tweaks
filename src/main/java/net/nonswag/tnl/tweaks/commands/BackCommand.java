package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.listener.api.command.CommandSource;
import net.nonswag.tnl.listener.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.command.exceptions.SourceMismatchException;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BackCommand extends TNLCommand {

    public BackCommand() {
        super("back", "tnl.back");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        if (source.isPlayer()) {
            TNLPlayer player = source.player();
            Location position = getLastPosition(player);
            if (position != null) {
                player.teleport(position);
                player.sendMessage("%prefix% §aYou got teleported");
            } else player.sendMessage("%prefix% §cNo last position set");
        } else throw new SourceMismatchException();
    }

    @Nullable
    public static Location getLastPosition(@Nonnull TNLPlayer player) {
        return player.getVirtualStorage().get("last-position", Location.class).getValue();
    }

    public static void setLastPosition(@Nonnull TNLPlayer player, @Nonnull Location location) {
        player.getVirtualStorage().put("last-position", location);
    }
}
