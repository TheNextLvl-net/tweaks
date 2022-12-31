package net.nonswag.tnl.tweaks.commands;

import net.nonswag.core.api.command.Invocation;
import net.nonswag.core.api.math.MathUtil;
import net.nonswag.core.api.message.Placeholder;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.command.exceptions.InvalidUseException;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.tweaks.commands.errors.RangeException;
import net.nonswag.tnl.tweaks.utils.Messages;

import java.util.ArrayList;
import java.util.List;

public class SpeedCommand extends TNLCommand {

    public SpeedCommand() {
        super("speed", "tnl.speed");
        setUsage("%prefix% §c/speed §8[§6Speed§8]");
    }

    @Override
    protected void execute(Invocation invocation) {
        TNLPlayer player = (TNLPlayer) invocation.source();
        String[] args = invocation.arguments();
        if (args.length < 1) throw new InvalidUseException(this);
        try {
            int value = Integer.parseInt(args[0]);
            MathUtil.validateRange(value, 0, 10);
            if (player.abilityManager().isFlying()) player.abilityManager().setFlySpeed(value / 10f);
            else player.abilityManager().setWalkSpeed(value / 10f);
            String mode = player.abilityManager().isFlying() ? "fly" : "walk";
            player.messenger().sendMessage(Messages.SET_SPEED, new Placeholder("mode", mode), new Placeholder("speed", value));
        } catch (NumberFormatException e) {
            throw new InvalidUseException(this);
        } catch (IllegalArgumentException e) {
            throw new RangeException(0, 10);
        }
    }

    @Override
    protected List<String> suggest(Invocation invocation) {
        List<String> suggestions = new ArrayList<>();
        for (int i = 0; i <= 10; i++) suggestions.add(String.valueOf(i));
        return suggestions;
    }
}
