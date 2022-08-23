package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.core.api.command.CommandSource;
import net.nonswag.tnl.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.command.exceptions.SourceMismatchException;
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
        CommandSource source = invocation.source();
        if (!source.isPlayer()) throw new SourceMismatchException();
        TNLPlayer player = (TNLPlayer) source.player();
        Location position = player.getManager(PositionManager.class).getLastPosition();
        if (position != null) {
            player.worldManager().teleport(position);
            player.messenger().sendMessage(Messages.TELEPORTED);
        } else player.messenger().sendMessage(Messages.NO_POSITION);
    }
}
