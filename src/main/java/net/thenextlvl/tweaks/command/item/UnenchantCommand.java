package net.thenextlvl.tweaks.command.item;

import core.api.placeholder.Placeholder;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.CommandSenderException;
import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

@CommandInfo(
        name = "unenchant",
        description = "unenchant your tools",
        permission = "tweaks.command.unenchant",
        usage = "/<command> [enchantment...]"
)
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
                var enchantment = Placeholder.<CommandSender>of("enchantment", arg);
                player.sendRichMessage(Messages.INVALID_ENCHANTMENT.message(player.locale(), player, enchantment));
                return true;
            }
            itemInMainHand.removeEnchantment(byKey);
        }

        inventory.setItemInMainHand(itemInMainHand);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player))
            return null;
        PlayerInventory inventory = player.getInventory();
        ItemStack itemInMainHand = inventory.getItemInMainHand();

        return itemInMainHand.getEnchantments().keySet().stream()
                .map(enchantment -> enchantment.getKey().asString())
                .filter(s -> !Arrays.asList(args).contains(s))
                .toList();
    }
}
