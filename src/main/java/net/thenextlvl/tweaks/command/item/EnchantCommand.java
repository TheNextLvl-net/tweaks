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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.suggestion.EnchantSuggestionProvider;
import org.bukkit.GameRule;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@NullMarked
public class EnchantCommand {
    private final TweaksPlugin plugin;

    public EnchantCommand(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Commands registrar) {
        var max = plugin.config().general.enchantmentOverflow ? 255 : RegistryAccess.registryAccess()
                .getRegistry(RegistryKey.ENCHANTMENT).stream()
                .mapToInt(Enchantment::getMaxLevel)
                .max().orElse(10);
        var command = Commands.literal(plugin.commands().enchant.command)
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.enchant"))
                .then(Commands.argument("enchantment", ArgumentTypes.resource(RegistryKey.ENCHANTMENT))
                        .suggests(new EnchantSuggestionProvider(plugin))
                        .then(Commands.argument("level", IntegerArgumentType.integer(1, max))
                                .suggests(this::suggestLevels)
                                .executes(context -> enchant(context, context.getArgument("level", int.class))))
                        .executes(context -> enchant(context, 1)))
                .build();
        registrar.register(command, "Enchant your tools", plugin.commands().enchant.aliases);
    }

    private CompletableFuture<Suggestions> suggestLevels(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        var enchantment = context.getLastChild().getArgument("enchantment", Enchantment.class);
        IntStream.rangeClosed(enchantment.getStartLevel(), enchantment.getMaxLevel())
                .mapToObj(String::valueOf)
                .filter(s -> s.contains(builder.getRemaining()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }

    private int enchant(CommandContext<CommandSourceStack> context, int level) {
        var player = (Player) context.getSource().getSender();
        var enchantment = context.getArgument("enchantment", Enchantment.class);
        var item = player.getInventory().getItemInMainHand();

        if (item.getType().isAir()) {
            plugin.bundle().sendMessage(player, "command.hold.item");
            return 0;
        }
        if (!plugin.config().general.unsafeEnchantments && !enchantment.canEnchantItem(item)) {
            plugin.bundle().sendMessage(player, "command.enchantment.applicable",
                    Placeholder.component("item", Component.translatable(item)));
            return 0;
        }

        if (!plugin.config().general.enchantmentOverflow)
            level = Math.min(level, enchantment.getMaxLevel());
        level = Math.max(level, enchantment.getStartLevel());

        item.addUnsafeEnchantment(enchantment, level);
        if (Boolean.TRUE.equals(player.getWorld().getGameRuleValue(GameRule.SEND_COMMAND_FEEDBACK)))
            plugin.bundle().sendMessage(player, "command.enchantment.applied",
                    Placeholder.component("enchantment", enchantment.displayName(level).style(Style.empty())));

        return Command.SINGLE_SUCCESS;
    }
}
