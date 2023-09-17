package net.thenextlvl.tweaks.command.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@CommandInfo(
        name = "enchant",
        usage = "/<command> [enchantment] (level)",
        permission = "tweaks.command.enchant",
        description = "enchant your tools"
)
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
        Enchantment enchantment = Enchantment.getByKey(namespacedKey);
        if (enchantment == null)
            return false;

        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItemInMainHand();

        if (item.getType().isEmpty()) {
            player.sendMessage(Component.translatable("tweaks.hold.item"));
            return true;
        }
        if (!enchantment.canEnchantItem(item)) {
            player.sendRichMessage("<lang:tweaks.enchantment.not.applicable>", Placeholder.component("item",
                    Component.translatable(item)));
            return true;
        }

        int level = enchantment.getStartLevel();
        if (args.length == 2) try {
            level = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            return false;
        }
        level = Math.min(enchantment.getMaxLevel(), Math.max(enchantment.getStartLevel(), level));

        int finalLevel = level;
        item.addEnchantment(enchantment, level);
        inventory.setItemInMainHand(item);
        player.sendRichMessage("<lang:tweaks.enchantment.applied>", Placeholder.component("enchantment",
                Component.translatable(enchantment)));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player))
            return null;
        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItemInMainHand();
        if (item.getType().isEmpty())
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
            Enchantment enchantment = Enchantment.getByKey(namespacedKey);
            if (enchantment == null)
                return null;
            if (enchantment.getStartLevel() == enchantment.getMaxLevel()) return null;
            return IntStream.range(enchantment.getStartLevel(), enchantment.getMaxLevel() + 1)
                    .mapToObj(Integer::toString).toList();
        }
        return null;
    }
}
