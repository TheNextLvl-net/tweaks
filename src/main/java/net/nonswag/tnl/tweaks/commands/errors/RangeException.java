package net.nonswag.tnl.tweaks.commands.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.nonswag.core.api.command.Invocation;
import net.nonswag.core.api.message.Placeholder;
import net.nonswag.tnl.listener.api.command.exceptions.CommandException;
import net.nonswag.tnl.tweaks.utils.Messages;

@Getter
@RequiredArgsConstructor
public class RangeException extends CommandException {
    private final int from, to;

    @Override
    public void handle(Invocation invocation) {
        invocation.source().sendMessage(Messages.NUMBER_BETWEEN, new Placeholder("first", getFrom()), new Placeholder("second", getTo()));
    }
}
