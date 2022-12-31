package net.nonswag.tnl.tweaks.commands.errors;

import net.nonswag.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.exceptions.CommandException;
import net.nonswag.tnl.tweaks.utils.Messages;

public class NothingChangedException extends CommandException {
    @Override
    public void handle(Invocation invocation) {
        invocation.source().sendMessage(Messages.NOTHING_CHANGED);
    }
}
