package net.thenextlvl.tweaks.command.server.suggestion;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.concurrent.CompletableFuture;

public class TagSuggestionProvider<S> implements SuggestionProvider<S> {


    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        builder.suggest("\\n", () -> "line break");
        builder.suggest("\\t", () -> "tab");
        builder.suggest("\\r", () -> "carriage return");
        builder.suggest("<newline>", () -> "line break");
        builder.suggest("<dark_blue>", () -> "dark blue");
        builder.suggest("<dark_green>", () -> "dark green");
        builder.suggest("<dark_aqua>", () -> "dark aqua");
        builder.suggest("<dark_red>", () -> "dark red");
        builder.suggest("<dark_purple>", () -> "dark purple");
        builder.suggest("<gold>", () -> "gold");
        builder.suggest("<gray>", () -> "gray");
        builder.suggest("<dark_gray>", () -> "dark gray");
        builder.suggest("<blue>", () -> "blue");
        builder.suggest("<black>", () -> "black");
        builder.suggest("<green>", () -> "green");
        builder.suggest("<aqua>", () -> "aqua");
        builder.suggest("<red>", () -> "red");
        builder.suggest("<purple>", () -> "purple");
        builder.suggest("<yellow>", () -> "yellow");
        builder.suggest("<white>", () -> "white");
        builder.suggest("<obfuscated>", () -> "obfuscated");
        builder.suggest("<bold>", () -> "bold");
        builder.suggest("<underlined>", () -> "underlined");
        builder.suggest("<strikethrough>", () -> "strikethrough");
        builder.suggest("<italic>", () -> "italic");
        return builder.buildFuture();
    }
}
