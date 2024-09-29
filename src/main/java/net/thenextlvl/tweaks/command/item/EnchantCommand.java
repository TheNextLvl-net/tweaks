package net.thenextlvl.tweaks.command.item;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.item.suggestion.EnchantmentSuggestionProvider;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@SuppressWarnings({"UnstableApiUsage", "unchecked"})
public class EnchantCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var literal = Commands.literal("enchant")
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.enchant"))
                .then(Commands.argument("enchantment", ArgumentTypes.resourceKey(RegistryKey.ENCHANTMENT))
                        .suggests(new EnchantmentSuggestionProvider())
                        .then(Commands.argument("level", IntegerArgumentType.integer(1, 255))
                                .suggests(this::suggestLevels)
                                .executes(context -> enchant(context, context.getArgument("level", int.class))))
                        .executes(context -> enchant(context, 1)))
                .build();
        registrar.register(literal, "Enchant your tools");
    }

    private CompletableFuture<Suggestions> suggestLevels(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        var key = (TypedKey<Enchantment>) context.getLastChild().getArgument("enchantment", TypedKey.class);
        var enchantment = RegistryAccess.registryAccess().getRegistry(key.registryKey()).getOrThrow(key);
        IntStream.rangeClosed(enchantment.getStartLevel(), enchantment.getMaxLevel()).forEach(builder::suggest);
        return builder.buildFuture();
    }

    private int enchant(CommandContext<CommandSourceStack> context, int level) {
        var player = (Player) context.getSource().getSender();
        var key = (TypedKey<Enchantment>) context.getArgument("enchantment", TypedKey.class);
        var enchantment = RegistryAccess.registryAccess().getRegistry(key.registryKey()).getOrThrow(key);

        var item = player.getInventory().getItemInMainHand();

        if (item.getType().isEmpty()) {
            plugin.bundle().sendMessage(player, "hold.item");
            return 0;
        }
        if (!enchantment.canEnchantItem(item)) {
            plugin.bundle().sendMessage(player, "enchantment.not.applicable",
                    Placeholder.component("item", Component.translatable(item)));
            return 0;
        }

        level = Math.min(enchantment.getMaxLevel(), Math.max(enchantment.getStartLevel(), level)); // todo: enchantment overflow setting

        item.addEnchantment(enchantment, level);
        plugin.bundle().sendMessage(player, "enchantment.applied", Placeholder.component("enchantment",
                enchantment.displayName(level).style(Style.empty())));

        return Command.SINGLE_SUCCESS;
    }
}
