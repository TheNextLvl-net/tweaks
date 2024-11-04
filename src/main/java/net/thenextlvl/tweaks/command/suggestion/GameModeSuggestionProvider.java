package net.thenextlvl.tweaks.command.suggestion;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import core.paper.command.SuggestionProvider;
import org.bukkit.GameMode;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

@NullMarked
public class GameModeSuggestionProvider implements SuggestionProvider {
    @Override
    public CompletableFuture<Suggestions> suggest(CommandContext<?> context, SuggestionsBuilder builder) {
        Arrays.stream(GameMode.values())
                .map(gameMode -> gameMode.name().toLowerCase())
                .filter(name -> name.contains(builder.getRemaining()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}
