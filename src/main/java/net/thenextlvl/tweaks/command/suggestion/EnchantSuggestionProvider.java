package net.thenextlvl.tweaks.command.suggestion;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public final class EnchantSuggestionProvider implements SuggestionProvider<CommandSourceStack> {
    private final TweaksPlugin plugin;

    public EnchantSuggestionProvider(final TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(final CommandContext<CommandSourceStack> context, final SuggestionsBuilder builder) {
        if (!(context.getSource().getSender() instanceof final Player player)) return builder.buildFuture();
        final var item = player.getInventory().getItemInMainHand();
        RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).stream()
                .filter(enchantment -> plugin.config().general.unsafeEnchantments || enchantment.canEnchantItem(item))
                .filter(enchantment -> plugin.config().general.unsafeEnchantments
                                       || item.getEnchantments().containsKey(enchantment)
                                       || item.getEnchantments().keySet().stream().noneMatch(enchantment::conflictsWith))
                .map(enchantment -> enchantment.key().asString())
                .filter(s -> s.contains(builder.getRemaining()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}
