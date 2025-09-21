package net.thenextlvl.tweaks.command.suggestion;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.model.NamedLocation;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public final class WarpSuggestionProvider implements SuggestionProvider<CommandSourceStack> {
    private final TweaksPlugin plugin;

    public WarpSuggestionProvider(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        return plugin.warpController().getWarps().thenApply(warps -> {
            warps.stream()
                    .map(NamedLocation::getName)
                    .map(StringArgumentType::escapeIfRequired)
                    .filter(name -> name.contains(builder.getRemaining()))
                    .forEach(builder::suggest);
            return builder.build();
        });
    }
}
