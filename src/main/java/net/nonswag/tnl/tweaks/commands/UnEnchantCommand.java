package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.core.api.command.CommandSource;
import net.nonswag.tnl.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.command.exceptions.SourceMismatchException;
import net.nonswag.tnl.listener.api.enchantment.Enchant;
import net.nonswag.tnl.listener.api.item.ItemType;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class UnEnchantCommand extends TNLCommand {

    public UnEnchantCommand() {
        super("unenchant", "tnl.unenchant");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (source.isPlayer()) {
            TNLPlayer player = (TNLPlayer) source.player();
            if (args.length >= 1) {
                Enchantment enchantment = getEnchantment(args[0]);
                if (enchantment != null) {
                    ItemStack item = player.getInventory().getItemInMainHand();
                    if (ItemType.AIR.matches(item)) item = player.getInventory().getItemInOffHand();
                    if (!ItemType.AIR.matches(item)) {
                        item.removeEnchantment(enchantment);
                        player.sendMessage("%prefix% §aUnenchanted the item");
                    } else player.sendMessage("%prefix% §cHold an item in your hand");
                } else player.sendMessage("%prefix% §c/unenchant §8[§6Enchantment§8]");
            } else player.sendMessage("%prefix% §c/unenchant §8[§6Enchantment§8]");
        } else throw new SourceMismatchException();
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
        }
        return suggestions;
    }
}
