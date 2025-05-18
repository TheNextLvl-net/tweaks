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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ItemCommand {
    private final TweaksPlugin plugin;

    public ItemCommand(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().item.command)
                .requires(stack -> stack.getSender() instanceof Player player
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

    private int item(CommandContext<CommandSourceStack> context, int amount) {
        var player = (Player) context.getSource().getSender();
        var item = context.getArgument("item", ItemStack.class);
        var inventory = player.getInventory();

        var added = 0;
        do {
            var min = Math.min(amount, item.getMaxStackSize());
            item.setAmount(min);

            var leftover = inventory.addItem(item);
            var leftovers = leftover.values().stream()
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

        plugin.bundle().sendMessage(player, "command.item.received",
                Formatter.number("amount", added),
                Placeholder.component("item", Component.translatable(item)
                        .hoverEvent(item.asHoverEvent(showItem -> showItem))));

        return Command.SINGLE_SUCCESS;
    }
}
