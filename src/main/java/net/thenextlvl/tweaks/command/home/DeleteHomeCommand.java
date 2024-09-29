package net.thenextlvl.tweaks.command.home;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.suggestion.HomeSuggestionProvider;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class DeleteHomeCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().deleteHome().command())
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("extra-tweaks.home.delete"))
                .then(Commands.argument("name", StringArgumentType.string())
                        .suggests(new HomeSuggestionProvider(plugin))
                        .executes(context -> deleteHome(context, context.getArgument("name", String.class))))
                .executes(context -> deleteHome(context, plugin.config().homes().unnamedName()))
                .build();
        registrar.register(command, "Delete your homes", plugin.commands().deleteHome().aliases());
    }

    private int deleteHome(CommandContext<CommandSourceStack> context, String name) {
        var player = (Player) context.getSource().getSender();
        plugin.homeController().deleteHome(player, name).thenAccept(success -> {
            var message = success ? "command.home.delete.name" : "command.home.unknown";
            plugin.bundle().sendMessage(player, message, Placeholder.parsed("name", name));
        });
        return Command.SINGLE_SUCCESS;
    }
}
