package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.listener.api.command.CommandSource;
import net.nonswag.tnl.listener.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.message.Message;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class SpeedCommand extends TNLCommand {

    public SpeedCommand() {
        super("speed", "tnl.speed");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (source.isPlayer()) {
            if (args.length >= 1) {
                try {
                    int value = Integer.parseInt(args[0]);
                    if (value >= 0 && value <= 10) {
                        if (source.player().isFlying()) source.player().setFlySpeed(value / 10f);
                        else source.player().setWalkSpeed(value / 10f);
                        String mode = source.player().isFlying() ? "fly" : "walk";
                        source.sendMessage("%prefix% §aSet your §8(§7" + mode + "§8)§a speed to §6" + value);
                    } else source.sendMessage("%prefix% §cUse a number between §40§c and §410");
                } catch (Exception e) {
                    source.sendMessage("%prefix% §c/speed §8[§6Speed§8]");
                }
            } else source.sendMessage("%prefix% §c/speed §8[§6Speed§8]");
        } else source.sendMessage(Message.CONSOLE_COMMAND_EN.getText());
    }

    @Nonnull
    @Override
    protected List<String> suggest(@Nonnull Invocation invocation) {
        List<String> suggestions = new ArrayList<>();
        for (int i = 0; i <= 10; i++) suggestions.add(String.valueOf(i));
        return suggestions;
    }
}
