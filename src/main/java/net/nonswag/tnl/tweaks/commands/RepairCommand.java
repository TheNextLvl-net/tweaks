package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.core.api.command.CommandSource;
import net.nonswag.tnl.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.command.exceptions.SourceMismatchException;
import net.nonswag.tnl.listener.api.item.ItemType;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
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
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (source.isPlayer()) {
            TNLPlayer player = (TNLPlayer) source.player();
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("all")) {
                    int i = 0;
                    for (@Nullable ItemStack item : player.inventoryManager().getInventory().getContents()) {
                        if (item != null && item.getItemMeta() instanceof Damageable d && d.hasDamage()) {
                            d.setDamage(0);
                            item.setItemMeta((ItemMeta) d);
                            i++;
                        }
                    }
                    if (i == 0) player.messenger().sendMessage("%prefix% §cNo items could be repaired");
                    else player.messenger().sendMessage("%prefix% §aRepaired §6" + i + "§a item" + (i != 1 ? "s" : ""));
                } else player.messenger().sendMessage("%prefix% §c/repair all");
            } else {
                ItemStack item = player.inventoryManager().getInventory().getItemInMainHand();
                if (ItemType.AIR.matches(item)) item = player.inventoryManager().getInventory().getItemInOffHand();
                if (!ItemType.AIR.matches(item)) {
                    if (item.getItemMeta() instanceof Damageable d && d.hasDamage()) {
                        d.setDamage(0);
                        item.setItemMeta((ItemMeta) d);
                        player.messenger().sendMessage("%prefix% §aRepaired your item");
                    } else player.messenger().sendMessage("%prefix% §cYour item has no damage");
                } else player.messenger().sendMessage("%prefix% §cHold an item in your hand");
            }
        } else throw new SourceMismatchException();
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