package net.thenextlvl.tweaks.command.home;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.suggestion.HomeSuggestionProvider;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.COMMAND;

@NullMarked
public class HomeCommand {
    private final TweaksPlugin plugin;

    public HomeCommand(final TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(final Commands registrar) {
        final var command = Commands.literal(plugin.commands().home.command)
                .requires(stack -> stack.getSender() instanceof final Player player
                                   && player.hasPermission("tweaks.command.home"))
                .then(Commands.argument("name", StringArgumentType.string())
                        .suggests(new HomeSuggestionProvider(plugin))
                        .executes(context -> home(context, context.getArgument("name", String.class))))
                .executes(context -> home(context, plugin.config().homes.unnamedName))
                .build();
        registrar.register(command, "Teleport to your homes", plugin.commands().home.aliases);
    }

    private int home(final CommandContext<CommandSourceStack> context, final String name) {
        final var player = (Player) context.getSource().getSender();
        plugin.homeController().getHome(player, name).thenAccept(home -> home.ifPresentOrElse(location ->
                plugin.teleportController().teleport(player, location, COMMAND).thenAccept(success -> {
                    final var message = success ? "command.home.name" : "command.teleport.cancelled";
                    plugin.bundle().sendMessage(player, message, Placeholder.parsed("name", name));
                }), () -> plugin.bundle().sendMessage(player, "command.home.unknown", Placeholder.parsed("name", name))));
        return Command.SINGLE_SUCCESS;
    }
}
