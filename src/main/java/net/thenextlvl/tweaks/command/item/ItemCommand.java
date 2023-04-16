package net.thenextlvl.tweaks.command.item;

import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.CommandSenderException;
import net.thenextlvl.tweaks.command.api.InvalidItemException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@CommandInfo(
        name = "item",
        usage = "/item [item] (amount)",
        description = "gives you an item of your choice",
        permission = "tweaks.command.item",
        aliases = {"i"}
)
public class ItemCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) throw new CommandSenderException();
        if (args.length < 1) return false;


        ItemStack stack;
        try {
            stack = Bukkit.getItemFactory().createItemStack(args[0]);
        } catch (IllegalArgumentException e) {
            throw new InvalidItemException(args[0]);
        }

        int amount = 1;
        if (args.length == 2) {
            try {
                amount = Math.max(1, Math.min(Integer.parseInt(args[1]), 2500));
            } catch (NumberFormatException e) {
                return false;
            }
        }

        PlayerInventory inventory = player.getInventory();
        do {
            int min = Math.min(amount, stack.getMaxStackSize());
            stack.setAmount(min);
            inventory.addItem(stack);
            amount -= min;
        } while (amount > 0);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return Arrays.stream(Material.values()).map(material -> material.getKey().asString()).toList();
        }
        if (args.length == 2) {
            if (args[1].isEmpty())
                return IntStream.range(1, 10).mapToObj(Integer::toString).toList();
            try {
                Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                return null;
            }
            return IntStream.range(0, 10).mapToObj(i -> args[1] + i).toList();
        }
        return null;
    }
}
