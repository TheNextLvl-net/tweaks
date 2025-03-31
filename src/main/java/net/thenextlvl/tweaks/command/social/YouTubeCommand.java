package net.thenextlvl.tweaks.command.social;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
@RequiredArgsConstructor
public class YouTubeCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().youtube().command()).executes(context -> {
            plugin.bundle().sendMessage(context.getSource().getSender(), "social.youtube");
            return Command.SINGLE_SUCCESS;
        }).build();
        registrar.register(command, "Subscribe to us on YouTube", plugin.commands().youtube().aliases());
    }
}
