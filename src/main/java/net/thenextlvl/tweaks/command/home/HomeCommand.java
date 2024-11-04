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
import org.jspecify.annotations.NullMarked;

import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.COMMAND;

@NullMarked
@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class HomeCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().home().command())
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.home"))
                .then(Commands.argument("name", StringArgumentType.string())
                        .suggests(new HomeSuggestionProvider(plugin))
                        .executes(context -> home(context, context.getArgument("name", String.class))))
                .executes(context -> home(context, plugin.config().homes().unnamedName()))
                .build();
        registrar.register(command, "Teleport to your homes", plugin.commands().home().aliases());
    }

    private int home(CommandContext<CommandSourceStack> context, String name) {
        var player = (Player) context.getSource().getSender();
        plugin.homeController().getHome(player, name).thenAccept(home -> {
            if (home == null) {
                plugin.bundle().sendMessage(player, "command.home.unknown", Placeholder.parsed("name", name));
            } else plugin.teleportController().teleport(player, home, COMMAND).thenAccept(success -> {
                var message = success ? "command.home.name" : "command.teleport.cancelled";
                plugin.bundle().sendMessage(player, message, Placeholder.parsed("name", name));
            });
        });
        return Command.SINGLE_SUCCESS;
    }
}
