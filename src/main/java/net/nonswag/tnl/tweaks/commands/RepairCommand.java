package net.nonswag.tnl.tweaks.commands;

import net.nonswag.core.api.command.CommandSource;
import net.nonswag.core.api.command.Invocation;
import net.nonswag.core.api.message.Placeholder;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.command.exceptions.InvalidUseException;
import net.nonswag.tnl.listener.api.item.ItemType;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.tweaks.utils.Messages;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RepairCommand extends TNLCommand {

    public RepairCommand() {
        super("repair", "tnl.repair");
        setUsage("%prefix% §c/repair all");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        TNLPlayer player = (TNLPlayer) source.player();
        if (args.length >= 1) {
            if (!args[0].equalsIgnoreCase("all")) throw new InvalidUseException(this);
            int i = 0;
            for (@Nullable ItemStack item : player.inventoryManager().getInventory().getContents()) {
                if (item == null || !(item.getItemMeta() instanceof Damageable d) || !d.hasDamage()) continue;
                d.setDamage(0);
                item.setItemMeta((ItemMeta) d);
                i++;
            }
            if (i == 0) player.messenger().sendMessage(Messages.NOTHING_REPAIRED);
            else if (i == 1) player.messenger().sendMessage(Messages.REPAIRED_ITEM);
            else player.messenger().sendMessage(Messages.REPAIRED_MULTIPLE_ITEMS, new Placeholder("amount", i));
        } else {
            ItemStack item = player.inventoryManager().getInventory().getItemInMainHand();
            if (ItemType.AIR.matches(item)) item = player.inventoryManager().getInventory().getItemInOffHand();
            if (!ItemType.AIR.matches(item)) {
                if (item.getItemMeta() instanceof Damageable d && d.hasDamage()) {
                    d.setDamage(0);
                    item.setItemMeta((ItemMeta) d);
                    player.messenger().sendMessage(Messages.REPAIRED_ITEM);
                } else player.messenger().sendMessage(Messages.NO_DAMAGE);
            } else player.messenger().sendMessage(Messages.HOLD_ITEM);
        }
    }

    @Override
    public boolean canUse(@Nonnull CommandSource source) {
        return source.isPlayer();
    }

    @Nonnull
    @Override
    protected List<String> suggest(@Nonnull Invocation invocation) {
        List<String> suggestions = new ArrayList<>();
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (source.isPlayer() && args.length <= 1) suggestions.add("all");
        return suggestions;
    }
}
