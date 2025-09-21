package net.thenextlvl.tweaks.command.suggestion;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public class MSGSuggestionProvider implements SuggestionProvider<CommandSourceStack> {
    private final TweaksPlugin plugin;

    public MSGSuggestionProvider(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        plugin.getServer().getOnlinePlayers().stream()
                .filter(player -> !player.equals(context.getSource().getSender()))
                .filter(player -> context.getSource().getSender() instanceof Player sender
                                  && plugin.msgController().isConversing(sender, player)
                                  || !plugin.dataController().isMsgToggled(player))
                .map(Player::getName)
                .map(StringArgumentType::escapeIfRequired)
                .filter(s -> s.contains(builder.getRemaining()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}
