package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.core.api.command.CommandSource;
import net.nonswag.tnl.core.api.command.Invocation;
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

public class UnEnchantCommand extends TNLCommand {

    public UnEnchantCommand() {
        super("unenchant", "tnl.unenchant");
        setUsage("%prefix% §c/unenchant §8[§6Enchantment§8]");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        TNLPlayer player = (TNLPlayer) source.player();
        if (args.length < 1) throw new InvalidUseException(this);
        Enchantment enchantment = getEnchantment(args[0]);
        if (enchantment == null) throw new InvalidUseException(this);
        ItemStack item = player.inventoryManager().getInventory().getItemInMainHand();
        if (ItemType.AIR.matches(item)) item = player.inventoryManager().getInventory().getItemInOffHand();
        if (!ItemType.AIR.matches(item)) {
            item.removeEnchantment(enchantment);
            player.messenger().sendMessage(Messages.UNENCHANTED_ITEM);
        } else player.messenger().sendMessage(Messages.HOLD_ITEM);
    }

    @Override
    public boolean canUse(@Nonnull CommandSource source) {
        return source.isPlayer();
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
        if (invocation.arguments().length > 1) return suggestions;
        Enchant.getEnchants().forEach(enchant -> suggestions.add(enchant.getKey().toString()));
        for (Enchantment enchantment : Enchantment.values()) suggestions.add(enchantment.getKey().toString());
        return suggestions;
    }
}
