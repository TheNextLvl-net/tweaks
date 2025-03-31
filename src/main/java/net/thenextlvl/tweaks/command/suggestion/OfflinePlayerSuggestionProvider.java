package net.thenextlvl.tweaks.command.suggestion;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.OfflinePlayer;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@NullMarked
@RequiredArgsConstructor
public class OfflinePlayerSuggestionProvider implements SuggestionProvider<CommandSourceStack> {
    private final TweaksPlugin plugin;

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        Arrays.stream(plugin.getServer().getOfflinePlayers())
                .map(OfflinePlayer::getName)
                .filter(Objects::nonNull)
                .filter(s -> s.contains(builder.getRemaining()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}
