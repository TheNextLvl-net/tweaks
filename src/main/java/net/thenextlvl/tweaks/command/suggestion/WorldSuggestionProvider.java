package net.thenextlvl.tweaks.command.suggestion;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.environment.WorldCommand;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class WorldSuggestionProvider<S> implements SuggestionProvider<S> {
    private final TweaksPlugin plugin;
    private final WorldCommand command;

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        plugin.getServer().getWorlds().stream()
                .filter(command::isWorldAffected)
                .map(world -> world.key().asString())
                .filter(name -> name.contains(builder.getRemaining()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}
