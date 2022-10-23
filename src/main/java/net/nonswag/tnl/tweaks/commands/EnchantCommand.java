package net.nonswag.tnl.tweaks.commands;

import net.nonswag.core.api.command.CommandSource;
import net.nonswag.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.command.exceptions.InvalidUseException;
import net.nonswag.tnl.listener.api.enchantment.Enchant;
import net.nonswag.tnl.listener.api.item.ItemType;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.tweaks.utils.Messages;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class EnchantCommand extends TNLCommand {

    public EnchantCommand() {
        super("enchant", "tnl.enchant");
        setUsage("%prefix% §c/enchant §8[§6Enchantment§8] §8(§6Level§8)");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        TNLPlayer player = (TNLPlayer) invocation.source();
        String[] args = invocation.arguments();
        if (args.length < 1) throw new InvalidUseException(this);
        Enchantment enchantment = getEnchantment(args[0]);
        if (enchantment == null) throw new InvalidUseException(this);
        ItemStack item = player.inventoryManager().getInventory().getItemInMainHand();
        if (ItemType.AIR.matches(item)) item = player.inventoryManager().getInventory().getItemInOffHand();
        if (!ItemType.AIR.matches(item)) {
            if (args.length >= 2) {
                try {
                    int level = Integer.parseInt(args[1]);
                    item.addUnsafeEnchantment(enchantment, level);
                    player.messenger().sendMessage(Messages.ENCHANTED_ITEM);
                } catch (NumberFormatException ignored) {
                    player.messenger().sendMessage("%prefix% §c/enchant " + enchantment.getKey() + " §8(§6Level§8)");
                }
            } else {
                item.addUnsafeEnchantment(enchantment, enchantment.getStartLevel());
                player.messenger().sendMessage(Messages.ENCHANTED_ITEM);
            }
        } else player.messenger().sendMessage(Messages.HOLD_ITEM);
    }

    @Override
    public boolean canUse(@Nonnull CommandSource source) {
        return source instanceof TNLPlayer;
    }

    @Nullable
    private Enchantment getEnchantment(@Nonnull String string) {
        for (Enchant enchant : Enchant.getEnchants()) {
            if (string.equalsIgnoreCase(enchant.getKey().toString())) return enchant;
        }
        for (Enchantment enchantment : Enchantment.values()) {
            if (string.equalsIgnoreCase(enchantment.getKey().toString())) return enchantment;
        }
        return null;
    }

    @Nonnull
    @Override
    protected List<String> suggest(@Nonnull Invocation invocation) {
        List<String> suggestions = new ArrayList<>();
        String[] args = invocation.arguments();
        if (args.length <= 1) {
            Enchant.getEnchants().forEach(enchant -> suggestions.add(enchant.getKey().toString()));
            for (Enchantment enchantment : Enchantment.values()) suggestions.add(enchantment.getKey().toString());
        } else if (args.length == 2) {
            Enchantment enchantment = getEnchantment(args[0]);
            if (enchantment == null) return suggestions;
            for (int i = enchantment.getStartLevel(); i <= enchantment.getMaxLevel(); i++) {
                suggestions.add(String.valueOf(i));
            }
        }
        return suggestions;
    }
}
