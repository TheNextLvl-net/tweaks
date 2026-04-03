package net.thenextlvl.tweaks.command.item;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.suggestion.UnenchantSuggestionProvider;
import org.bukkit.GameRules;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class UnenchantCommand {
    private final TweaksPlugin plugin;

    public UnenchantCommand(final TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(final Commands registrar) {
        final var command = Commands.literal(plugin.commands().unenchant.command)
                .requires(stack -> stack.getSender() instanceof final Player player
                                   && player.hasPermission("tweaks.command.unenchant"))
                .then(Commands.argument("enchantment", ArgumentTypes.resource(RegistryKey.ENCHANTMENT))
                        .suggests(new UnenchantSuggestionProvider())
                        .executes(this::unenchant))
                .build();
        registrar.register(command, "Unenchant your tools", plugin.commands().unenchant.aliases);
    }

    private int unenchant(final CommandContext<CommandSourceStack> context) {
        final var player = (Player) context.getSource().getSender();
        final var item = player.getInventory().getItemInMainHand();

        final var enchantment = context.getArgument("enchantment", Enchantment.class);
        final var level = item.removeEnchantment(enchantment);

        final var message = level != 0 ? "command.enchantment.removed" : "command.enchantment.absent";
        if (Boolean.TRUE.equals(player.getWorld().getGameRuleValue(GameRules.SEND_COMMAND_FEEDBACK)) || level == 0) {
            final var clamped = Math.clamp(level, enchantment.getStartLevel(), enchantment.getMaxLevel());
            plugin.bundle().sendMessage(player, message, Placeholder.component("enchantment",
                    enchantment.displayName(clamped).style(Style.empty())));
        }
        return level != 0 ? Command.SINGLE_SUCCESS : 0;
    }
}
