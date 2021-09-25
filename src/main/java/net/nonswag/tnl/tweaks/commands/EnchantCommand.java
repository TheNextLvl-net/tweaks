package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.listener.api.command.CommandSource;
import net.nonswag.tnl.listener.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.enchantment.Enchant;
import net.nonswag.tnl.listener.api.item.TNLItemType;
import net.nonswag.tnl.listener.api.message.Message;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class EnchantCommand extends TNLCommand {

    public EnchantCommand() {
        super("enchant", "tnl.enchant");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (source.isPlayer()) {
            TNLPlayer player = source.player();
            if (args.length >= 1) {
                Enchantment enchantment = getEnchantment(args[0]);
                if (enchantment != null) {
                    ItemStack item = player.getInventory().getItemInMainHand();
                    if (TNLItemType.AIR.matches(item)) {
                        item = player.getInventory().getItemInOffHand();
                    }
                    if (!TNLItemType.AIR.matches(item)) {
                        if (args.length >= 2) {
                            try {
                                int level = Integer.parseInt(args[1]);
                                item.addUnsafeEnchantment(enchantment, level);
                                player.sendMessage("%prefix% §aEnchanted the item");
                            } catch (NumberFormatException ignored) {
                                player.sendMessage("%prefix% §c/enchant " + enchantment.getKey() + " §8(§6Level§8)");
                            }
                        } else {
                            item.addUnsafeEnchantment(enchantment, enchantment.getStartLevel());
                            player.sendMessage("%prefix% §aEnchanted the item");
                        }
                    } else player.sendMessage("%prefix% §cHold an item in your hand");
                } else player.sendMessage("%prefix% §c/enchant §8[§6Enchantment§8] §8(§6Level§8)");
            } else player.sendMessage("%prefix% §c/enchant §8[§6Enchantment§8] §8(§6Level§8)");
        } else source.sendMessage(Message.PLAYER_COMMAND_EN.getText());
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
            for (Enchant enchant : Enchant.getEnchants()) suggestions.add(enchant.getKey().toString());
            for (Enchantment enchantment : Enchantment.values()) suggestions.add(enchantment.getKey().toString());
        } else if (args.length == 2) {
            Enchantment enchantment = getEnchantment(args[0]);
            if (enchantment != null) {
                for (int i = enchantment.getStartLevel(); i < enchantment.getMaxLevel(); i++) {
                    suggestions.add(String.valueOf(i));
                }
            }
        }
        return suggestions;
    }
}
