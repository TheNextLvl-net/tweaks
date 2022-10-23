package net.nonswag.tnl.tweaks.commands;

import net.nonswag.core.api.command.CommandSource;
import net.nonswag.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.tweaks.api.manager.PositionManager;
import net.nonswag.tnl.tweaks.utils.Messages;
import org.bukkit.Location;

import javax.annotation.Nonnull;

public class BackCommand extends TNLCommand {

    public BackCommand() {
        super("back", "tnl.back");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        TNLPlayer player = (TNLPlayer) invocation.source();
        Location position = player.getManager(PositionManager.class).getLastPosition();
        if (position != null) {
            player.worldManager().teleport(position);
            player.messenger().sendMessage(Messages.TELEPORTED);
        } else player.messenger().sendMessage(Messages.NO_POSITION);
    }

    @Override
    public boolean canUse(@Nonnull CommandSource source) {
        return source instanceof TNLPlayer;
    }
}
