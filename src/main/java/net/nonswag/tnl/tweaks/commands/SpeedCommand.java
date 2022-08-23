package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.core.api.command.CommandSource;
import net.nonswag.tnl.core.api.command.Invocation;
import net.nonswag.tnl.core.api.message.Placeholder;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.command.exceptions.InvalidUseException;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.tweaks.utils.Messages;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class SpeedCommand extends TNLCommand {

    public SpeedCommand() {
        super("speed", "tnl.speed");
        setUsage("%prefix% §c/speed §8[§6Speed§8]");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length < 1) throw new InvalidUseException(this);
        try {
            int value = Integer.parseInt(args[0]);
            if (value >= 0 && value <= 10) {
                TNLPlayer player = (TNLPlayer) source.player();
                if (player.abilityManager().isFlying()) player.abilityManager().setFlySpeed(value / 10f);
                else player.abilityManager().setWalkSpeed(value / 10f);
                String mode = player.abilityManager().isFlying() ? "fly" : "walk";
                source.sendMessage(Messages.SET_SPEED, new Placeholder("mode", mode), new Placeholder("speed", value));
            } else {
                source.sendMessage(Messages.NUMBER_BETWEEN, new Placeholder("first", 0), new Placeholder("second", 10));
            }
        } catch (NumberFormatException e) {
            throw new InvalidUseException(this);
        }
    }

    @Nonnull
    @Override
    protected List<String> suggest(@Nonnull Invocation invocation) {
        List<String> suggestions = new ArrayList<>();
        for (int i = 0; i <= 10; i++) suggestions.add(String.valueOf(i));
        return suggestions;
    }
}
