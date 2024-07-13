package net.thenextlvl.tweaks.command.item;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.CommandSenderException;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.IntStream;

@CommandInfo(
        name = "enchant",
        usage = "/<command> [enchantment] (level)",
        permission = "tweaks.command.enchant",
        description = "enchant your tools"
)
@RequiredArgsConstructor
public class EnchantCommand implements TabExecutor {
    private final TweaksPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player))
            throw new CommandSenderException();

        if (args.length < 1 || args.length > 2)
            return false;

        var namespacedKey = NamespacedKey.fromString(args[0]);
        if (namespacedKey == null)
            return false;

        var enchantment = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(namespacedKey);
        if (enchantment == null)
            return false;

        var item = player.getInventory().getItemInMainHand();

        if (item.getType().isEmpty()) {
            plugin.bundle().sendMessage(player, "hold.item");
            return true;
        }
        if (!enchantment.canEnchantItem(item)) {
            plugin.bundle().sendMessage(player, "enchantment.not.applicable", Placeholder.component("item",
                    Component.translatable(item)));
            return true;
        }

        var level = enchantment.getStartLevel();
        if (args.length == 2) try {
            level = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            return false;
        }
        level = Math.min(enchantment.getMaxLevel(), Math.max(enchantment.getStartLevel(), level));

        item.addEnchantment(enchantment, level);
        plugin.bundle().sendMessage(player, "enchantment.applied", Placeholder.component("enchantment",
                enchantment.displayName(level).style(Style.empty())));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player))
            return null;

        var item = player.getInventory().getItemInMainHand();
        if (item.getType().isEmpty())
            return null;

        if (args.length == 1) {
            return RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).stream()
                    .filter(enchantment -> enchantment.canEnchantItem(item)
                            && (item.getEnchantments().keySet().stream().noneMatch(enchantment::conflictsWith)
                            || item.getEnchantments().containsKey(enchantment)))
                    .map(enchantment -> enchantment.getKey().asString()).toList();
        }
        if (args.length == 2) {
            var namespacedKey = NamespacedKey.fromString(args[0]);
            if (namespacedKey == null)
                return null;

            var enchantment = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(namespacedKey);
            if (enchantment == null)
                return null;

            if (enchantment.getStartLevel() == enchantment.getMaxLevel()) return null;
            return IntStream.range(enchantment.getStartLevel(), enchantment.getMaxLevel() + 1)
                    .mapToObj(Integer::toString).toList();
        }
        return null;
    }
}
