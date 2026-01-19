package net.thenextlvl.tweaks.command.warp;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.suggestion.WarpSuggestionProvider;
import org.bukkit.GameRules;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class SetWarpCommand {
    private final TweaksPlugin plugin;

    public SetWarpCommand(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().setWarp.command)
                .requires(stack -> stack.getSender().hasPermission("tweaks.command.warp.set"))
                .then(Commands.argument("name", StringArgumentType.string())
                        .suggests(new WarpSuggestionProvider(plugin))
                        .executes(this::setWarp))
                .build();
        registrar.register(command, "Set a warp point", plugin.commands().setWarp.aliases);
    }

    private int setWarp(CommandContext<CommandSourceStack> context) {
        var name = context.getArgument("name", String.class);
        plugin.warpController().setWarp(name, context.getSource().getLocation()).thenAccept(success -> {
            var world = context.getSource().getLocation().getWorld();
            if (Boolean.FALSE.equals(world.getGameRuleValue(GameRules.SEND_COMMAND_FEEDBACK)) && success) return;
            var message = success ? "command.warp.set" : "nothing.changed";
            plugin.bundle().sendMessage(context.getSource().getSender(), message,
                    Placeholder.parsed("name", name));
        });
        return Command.SINGLE_SUCCESS;
    }
}
