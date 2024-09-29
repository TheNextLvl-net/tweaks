package net.thenextlvl.tweaks.command.player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.suggestion.OfflinePlayerSuggestionProvider;
import org.bukkit.entity.Player;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class SeenCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal("seen")
                .requires(stack -> stack.getSender().hasPermission("tweaks.command.seen"))
                .then(Commands.argument("player", StringArgumentType.word())
                        .suggests(new OfflinePlayerSuggestionProvider<>(plugin))
                        .executes(context -> {
                            plugin.getServer().getAsyncScheduler().runNow(plugin, task -> seen(context));
                            return Command.SINGLE_SUCCESS;
                        }))
                .build();
        registrar.register(command, "Gives you information about a player", List.of("find"));
    }

    private void seen(CommandContext<CommandSourceStack> context) {
        var sender = context.getSource().getSender();
        var name = context.getArgument("player", String.class);
        var player = plugin.getServer().getOfflinePlayer(name);

        if (player.getPlayer() != null) {
            plugin.bundle().sendMessage(sender, "last.seen.now", Placeholder.parsed("player",
                    player.getName() != null ? player.getName() : name));
            return;
        }

        if (!player.hasPlayedBefore()) {
            plugin.bundle().sendMessage(sender, "player.not.found", Placeholder.parsed("player",
                    player.getName() != null ? player.getName() : name));
            return;
        }

        var lastSeen = new Date(player.getLastSeen());
        var locale = sender instanceof Player p ? p.locale() : Locale.US;
        var format = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

        plugin.bundle().sendMessage(sender, "last.seen.time",
                Placeholder.parsed("player", player.getName() != null ? player.getName() : name),
                Placeholder.parsed("time", format.format(lastSeen)));
    }
}
