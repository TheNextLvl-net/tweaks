package net.thenextlvl.tweaks.command.warp;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.suggestion.WarpSuggestionProvider;
import org.bukkit.GameRule;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class DeleteWarpCommand {
    private final TweaksPlugin plugin;

    public DeleteWarpCommand(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().deleteWarp.command)
                .requires(stack -> stack.getSender().hasPermission("tweaks.command.warp.delete"))
                .then(Commands.argument("name", StringArgumentType.string())
                        .suggests(new WarpSuggestionProvider(plugin))
                        .executes(this::deleteWarp))
                .build();
        registrar.register(command, "Delete a warp point", plugin.commands().deleteWarp.aliases);
    }

    private int deleteWarp(CommandContext<CommandSourceStack> context) {
        var name = context.getArgument("name", String.class);
        plugin.warpController().deleteWarp(name).thenAccept(success -> {
            var world = context.getSource().getLocation().getWorld();
            if (Boolean.FALSE.equals(world.getGameRuleValue(GameRule.SEND_COMMAND_FEEDBACK)) && success) return;
            var message = success ? "command.warp.delete" : "command.warp.unknown";
            plugin.bundle().sendMessage(context.getSource().getSender(), message,
                    Placeholder.parsed("name", name));
        });
        return Command.SINGLE_SUCCESS;
    }
}
