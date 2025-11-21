package net.thenextlvl.tweaks.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@NullMarked
public final class GameModeArgumentType implements CustomArgumentType<GameMode, String> {
    @Override
    public GameMode parse(StringReader reader) throws CommandSyntaxException {
        var input = getNativeType().parse(reader);
        return Arrays.stream(GameMode.values()).filter(gameMode -> {
            return gameMode.name().toLowerCase(Locale.ROOT).startsWith(input.toLowerCase(Locale.ROOT))
                   || input.equals(String.valueOf(gameMode.getValue()));
        }).findAny().orElseThrow(() -> {
            var gameMode = Component.translatable("argument.gamemode.invalid", () -> Component.text(input));
            return new SimpleCommandExceptionType(MessageComponentSerializer.message().serialize(gameMode))
                    .createWithContext(reader);
        });
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Arrays.stream(GameMode.values())
                .map(gameMode -> gameMode.name().toLowerCase())
                .filter(name -> name.contains(builder.getRemaining()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }
}
