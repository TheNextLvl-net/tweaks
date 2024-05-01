package net.thenextlvl.tweaks.command.item;

import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.CommandSenderException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@CommandInfo(
        name = "lore",
        permission = "tweaks.command.lore",
        description = "change the lore of your items",
        usage = "/<command> [set|append|unset] (lore...)"
)
@RequiredArgsConstructor
public class LoreCommand implements TabExecutor {
    private final TweaksPlugin plugin;

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) throw new CommandSenderException();
        if (args.length < 1) return false;

        var item = player.getInventory().getItemInMainHand();

        if (item.getType().isEmpty()) {
            plugin.bundle().sendMessage(player, "hold.item");
            return true;
        }

        if (args.length < 2 && !args[0].equalsIgnoreCase("unset")) {
            plugin.bundle().sendMessage(player, "item.lore.tip");
            return false;
        }

        String lore = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).replace("\\t", "   ");
        String[] loreLines = lore.split("\\\\n");
        for (int i = 0; i < loreLines.length; i++) {
            loreLines[i] = ChatColor.translateAlternateColorCodes('&', loreLines[i]);
        }

        Consumer<? super ItemMeta> function = null;

        if (args[0].equalsIgnoreCase("unset"))
            function = unsetLore();

        if (args[0].equalsIgnoreCase("set"))
            function = setLore(loreLines);

        if (args[0].equalsIgnoreCase("append"))
            function = appendLore(loreLines);

        if (function == null) return false;

        if (!item.editMeta(function)) {
            plugin.bundle().sendMessage(player, "item.lore.fail");
            return false;
        }

        plugin.bundle().sendMessage(player, "item.lore.success");
        return true;
    }

    private static Consumer<ItemMeta> unsetLore() {
        return meta -> meta.lore(null);
    }

    @SuppressWarnings("deprecation")
    private Consumer<? super ItemMeta> setLore(String[] lore) {
        return itemMeta -> itemMeta.setLore(Arrays.asList(lore));
    }

    @SuppressWarnings("deprecation")
    private Consumer<? super ItemMeta> appendLore(String[] lore) {
        return itemMeta -> {
            var currentLore = Objects.requireNonNullElse(itemMeta.getLore(), new ArrayList<String>());
            currentLore.addAll(Arrays.asList(lore));
            itemMeta.setLore(currentLore);
        };
    }

    @Override
    public @Nullable List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) return Arrays.asList("set", "append", "unset");
        if (args.length >= 1 && args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("append"))
            return Arrays.asList(args[args.length - 1] + "\\n", args[args.length - 1] + "\\t");
        return null;
    }
}
