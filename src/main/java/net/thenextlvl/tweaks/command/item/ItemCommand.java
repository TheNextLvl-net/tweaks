package net.thenextlvl.tweaks.command.item;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.GameRules;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ItemCommand {
    private final TweaksPlugin plugin;

    public ItemCommand(final TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(final Commands registrar) {
        final var command = Commands.literal(plugin.commands().item.command)
                .requires(stack -> stack.getSender() instanceof final Player player
                                   && player.hasPermission("tweaks.command.item"))
                .then(Commands.argument("item", ArgumentTypes.itemStack())
                        .then(Commands.argument("amount", IntegerArgumentType.integer(1, 3564))
                                // 3564 is the theoretical maximum amount of items a player can hold
                                // 36 container slots * 99 items a stack can contain at max
                                .executes(context -> item(context, context.getArgument("amount", int.class))))
                        .executes(context -> item(context, 1)))
                .build();
        registrar.register(command, "Gives you an item of your choice", plugin.commands().item.aliases);
    }

    private int item(final CommandContext<CommandSourceStack> context, int amount) {
        final var player = (Player) context.getSource().getSender();
        final var item = context.getArgument("item", ItemStack.class);
        final var inventory = player.getInventory();

        var added = 0;
        do {
            final var min = Math.min(amount, item.getMaxStackSize());
            item.setAmount(min);

            final var leftover = inventory.addItem(item);
            final var leftovers = leftover.values().stream()
                    .mapToInt(ItemStack::getAmount)
                    .sum();

            added += min - leftovers;
            if (!leftover.isEmpty()) break;

            amount -= min;
        } while (amount > 0);

        if (added == 0) {
            plugin.bundle().sendMessage(player, "command.inventory.full");
            return 0;
        }

        if (Boolean.TRUE.equals(player.getWorld().getGameRuleValue(GameRules.SEND_COMMAND_FEEDBACK)))
            plugin.bundle().sendMessage(player, "command.item.received", Formatter.number("amount", added),
                    Placeholder.component("item", Component.translatable(item)
                            .hoverEvent(item.asHoverEvent(showItem -> showItem))));

        return Command.SINGLE_SUCCESS;
    }
}
