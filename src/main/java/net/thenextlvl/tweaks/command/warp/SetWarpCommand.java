package net.thenextlvl.tweaks.command.warp;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.suggestion.WarpSuggestionProvider;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class SetWarpCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().setWarp().command())
                .requires(stack -> stack.getSender().hasPermission("extra-tweaks.warp.set"))
                .then(Commands.argument("name", StringArgumentType.string())
                        .suggests(new WarpSuggestionProvider(plugin))
                        .executes(this::setWarp))
                .build();
        registrar.register(command, "Set warps", plugin.commands().setWarp().aliases());
    }

    private int setWarp(CommandContext<CommandSourceStack> context) {
        var name = context.getArgument("name", String.class);
        plugin.warpController().setWarp(name, context.getSource().getLocation())
                .thenAccept(unused -> plugin.bundle().sendMessage(context.getSource().getSender(),
                        "command.warp.set", Placeholder.parsed("name", name)))
                .exceptionally(throwable -> {
                    plugin.getComponentLogger().error("Failed to set warp", throwable);
                    return null;
                });
        return Command.SINGLE_SUCCESS;
    }
}