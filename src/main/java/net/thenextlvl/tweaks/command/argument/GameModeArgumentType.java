package net.thenextlvl.tweaks.command.argument;

import com.mojang.brigadier.arguments.StringArgumentType;
import core.paper.command.ComponentCommandExceptionType;
import core.paper.command.WrappedArgumentType;
import net.kyori.adventure.text.Component;
import net.thenextlvl.tweaks.command.suggestion.GameModeSuggestionProvider;
import org.bukkit.GameMode;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public class GameModeArgumentType extends WrappedArgumentType<String, GameMode> {
    public GameModeArgumentType() {
        super(StringArgumentType.word(), (reader, type) -> Arrays.stream(GameMode.values())
                .filter(mode -> mode.name().toLowerCase().startsWith(type)
                                || type.equalsIgnoreCase(String.valueOf(mode.getValue())))
                .findFirst().orElseThrow(() -> new ComponentCommandExceptionType(
                        Component.translatable("argument.gamemode.invalid", () -> Component.text(type))
                ).createWithContext(reader)), new GameModeSuggestionProvider());
    }
}
