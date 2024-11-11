package net.thenextlvl.tweaks.command.social;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class TikTokCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().tiktok().command()).executes(context -> {
            plugin.bundle().sendMessage(context.getSource().getSender(), "social.tiktok");
            return Command.SINGLE_SUCCESS;
        }).build();
        registrar.register(command, "Follow us on TikTok", plugin.commands().tiktok().aliases());
    }
}