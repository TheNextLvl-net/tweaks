package net.nonswag.tnl.tweaks.commands.errors;

import net.nonswag.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.exceptions.CommandException;
import net.nonswag.tnl.tweaks.utils.Messages;

public class InvalidValueException extends CommandException {

    @Override
    public void handle(Invocation invocation) {
        invocation.source().sendMessage(Messages.INVALID_VALUE);
    }
}
