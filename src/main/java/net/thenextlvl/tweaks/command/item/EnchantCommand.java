package net.thenextlvl.tweaks.command.item;

import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.CommandSenderException;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@CommandInfo(name = "enchant", usage = "/enchant [enchantment] (level)", permission = "tweaks.command.enchant", description = "enchant your tools")
public class EnchantCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player))
            throw new CommandSenderException();
        if (args.length < 1 || args.length > 2)
            return false;

        NamespacedKey namespacedKey = NamespacedKey.fromString(args[0]);
        if (namespacedKey == null)
            return false;
        Enchantment byKey = Enchantment.getByKey(namespacedKey);
        if (byKey == null)
            return false;

        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItemInMainHand();
        if (item.getType().isEmpty())
            return false;

        int level = byKey.getStartLevel();
        if (args.length == 2) {
            try {
                level = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                return false;
            }
        }

        item.addEnchantment(byKey, level);
        inventory.setItemInMainHand(item);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player))
            return null;
        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItemInMainHand();
        if (!item.getType().isEmpty())
            return null;

        if (args.length == 1) {
            return Arrays.stream(Enchantment.values())
                    .filter(enchantment -> enchantment.canEnchantItem(item))
                    .filter(enchantment -> item.getEnchantments().keySet().stream().noneMatch(enchantment::conflictsWith))
                    .map(enchantment -> enchantment.getKey().asString()).toList();
        }
        if (args.length == 2) {
            NamespacedKey namespacedKey = NamespacedKey.fromString(args[0]);
            if (namespacedKey == null)
                return null;
            Enchantment byKey = Enchantment.getByKey(namespacedKey);
            return IntStream.range(byKey.getStartLevel(), byKey.getMaxLevel() + 1).mapToObj(Integer::toString).toList();
        }
        return null;
    }
}
