package net.thenextlvl.tweaks.command.player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.suggestion.OfflinePlayerSuggestionProvider;
import org.jspecify.annotations.NullMarked;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@NullMarked
public class SeenCommand {
    private final TweaksPlugin plugin;

    public SeenCommand(final TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(final Commands registrar) {
        final var command = Commands.literal(plugin.commands().seen.command)
                .requires(stack -> stack.getSender().hasPermission("tweaks.command.seen"))
                .then(Commands.argument("player", StringArgumentType.word())
                        .suggests(new OfflinePlayerSuggestionProvider(plugin))
                        .executes(context -> {
                            plugin.getServer().getAsyncScheduler().runNow(plugin, task -> seen(context));
                            return Command.SINGLE_SUCCESS;
                        }))
                .build();
        registrar.register(command, "Gives you information about a player", plugin.commands().seen.aliases);
    }

    private void seen(final CommandContext<CommandSourceStack> context) {
        final var sender = context.getSource().getSender();
        final var name = context.getArgument("player", String.class);
        final var player = plugin.getServer().getOfflinePlayer(name);

        if (player.getPlayer() != null) {
            plugin.bundle().sendMessage(sender, "command.last.seen.now", Placeholder.parsed("player",
                    player.getName() != null ? player.getName() : name));
            return;
        }

        if (!player.hasPlayedBefore()) {
            plugin.bundle().sendMessage(sender, "player.not.found", Placeholder.parsed("player",
                    player.getName() != null ? player.getName() : name));
            return;
        }

        final var time = Instant.ofEpochMilli(player.getLastSeen());
        plugin.bundle().sendMessage(sender, "command.last.seen.time",
                Placeholder.parsed("player", player.getName() != null ? player.getName() : name),
                Formatter.date("date", LocalDateTime.ofInstant(time, ZoneId.systemDefault())));
    }
}
