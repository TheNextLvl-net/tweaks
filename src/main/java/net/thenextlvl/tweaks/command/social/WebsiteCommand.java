package net.thenextlvl.tweaks.command.social;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
@RequiredArgsConstructor
public class WebsiteCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().website().command()).executes(context -> {
            plugin.bundle().sendMessage(context.getSource().getSender(), "social.website");
            return Command.SINGLE_SUCCESS;
        }).build();
        registrar.register(command, "Retrieve the website link", plugin.commands().website().aliases());
    }
}
