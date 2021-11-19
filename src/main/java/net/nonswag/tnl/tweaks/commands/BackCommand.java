package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.core.api.command.CommandSource;
import net.nonswag.tnl.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.command.exceptions.SourceMismatchException;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.tweaks.utils.Messages;
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
            TNLPlayer player = (TNLPlayer) source.player();
            Location position = getLastPosition(player);
            if (position != null) {
                player.teleport(position);
                player.sendMessage(Messages.TELEPORTED);
            } else player.sendMessage(Messages.NO_POSITION);
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
