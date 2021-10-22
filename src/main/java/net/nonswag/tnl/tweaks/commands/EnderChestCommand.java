package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.core.api.command.CommandSource;
import net.nonswag.tnl.core.api.command.Invocation;
import net.nonswag.tnl.core.api.message.Placeholder;
import net.nonswag.tnl.core.api.message.key.MessageKey;
import net.nonswag.tnl.listener.TNLListener;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.command.exceptions.SourceMismatchException;
import net.nonswag.tnl.listener.api.player.TNLPlayer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class EnderChestCommand extends TNLCommand {

    public EnderChestCommand() {
        super("enderchest", "tnl.enderchest", "ec");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (source.isPlayer()) {
            TNLPlayer player = (TNLPlayer) source.player();
            if (args.length >= 1) {
                TNLPlayer arg = TNLPlayer.cast(args[0]);
                if (arg != null) {
                    player.openInventory(arg.getEnderChest());
                } else player.sendMessage(MessageKey.PLAYER_NOT_ONLINE, new Placeholder("player", args[0]));
            } else player.sendMessage("%prefix% §c/enderchest §8[§6Player§8]");
        } else throw new SourceMismatchException();
    }

    @Nonnull
    @Override
    protected List<String> suggest(@Nonnull Invocation invocation) {
        List<String> suggestions = new ArrayList<>();
        CommandSource source = invocation.source();
        if (source.isPlayer()) {
            for (TNLPlayer all : TNLListener.getInstance().getOnlinePlayers()) suggestions.add(all.getName());
        }
        return suggestions;
    }
}
