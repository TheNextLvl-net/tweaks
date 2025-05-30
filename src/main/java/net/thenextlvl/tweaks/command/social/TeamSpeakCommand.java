package net.thenextlvl.tweaks.command.social;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class TeamSpeakCommand {
    private final TweaksPlugin plugin;

    public TeamSpeakCommand(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().teamspeak.command).executes(context -> {
            plugin.bundle().sendMessage(context.getSource().getSender(), "social.teamspeak");
            return Command.SINGLE_SUCCESS;
        }).build();
        registrar.register(command, "Join our teamspeak", plugin.commands().teamspeak.aliases);
    }
}
