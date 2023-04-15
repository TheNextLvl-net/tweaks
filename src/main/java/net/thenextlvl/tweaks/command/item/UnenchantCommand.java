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
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

@CommandInfo(name = "unenchant", permission = "tweaks.command.unenchant", usage = "/<command> <enchantment...>")
public class UnenchantCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player))
            throw new CommandSenderException();

        if (args.length == 0)
            return false;

        PlayerInventory inventory = player.getInventory();
        ItemStack itemInMainHand = inventory.getItemInMainHand();

        for (String arg : args) {
            NamespacedKey namespacedKey = NamespacedKey.fromString(arg);
            Enchantment byKey = Enchantment.getByKey(namespacedKey);
            if (byKey == null) {
                // TODO: Give the player an information
                continue;
            }
            itemInMainHand.removeEnchantment(byKey);
        }

        inventory.setItemInMainHand(itemInMainHand);
        // TODO: Send a message
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player))
            return Collections.emptyList();
        PlayerInventory inventory = player.getInventory();
        ItemStack itemInMainHand = inventory.getItemInMainHand();

        return itemInMainHand.getEnchantments().keySet().stream()
                .map(enchantment -> enchantment.getKey().asString())
                .filter(s -> StringUtil.startsWithIgnoreCase(s, args[args.length - 1]))
                .toList();
    }
}
