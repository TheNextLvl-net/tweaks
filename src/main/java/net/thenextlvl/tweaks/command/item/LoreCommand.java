package net.thenextlvl.tweaks.command.item;

import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.CommandSenderException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@CommandInfo(
        name = "lore",
        permission = "tweaks.command.lore",
        description = "change the lore of your items",
        usage = "/<command> [set|append] [lore...]"
)
public class LoreCommand implements TabExecutor {
    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) throw new CommandSenderException();
        if (args.length < 2) return false;

        String lore = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).replace("\\t", "   ");
        String[] loreLines = lore.split("\\\\n");
        for (int i = 0; i < loreLines.length; i++) {
            loreLines[i] = ChatColor.translateAlternateColorCodes('&', loreLines[i]);
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItemInMainHand();

        Consumer<? super ItemMeta> function = null;

        if (args[0].equalsIgnoreCase("set")) {
            function = setLore(loreLines);
        }

        if (args[0].equalsIgnoreCase("append")) {
            function = appendLore(loreLines);
        }

        if (function == null) return false;

        if (!item.editMeta(function)) {
            // TODO: Something went wrong during updating the lore to the item in your main hand.
            return false;
        }

        inventory.setItemInMainHand(item);
        // TODO: The lore was successfully updated
        return true;
    }

    @SuppressWarnings("deprecation")
    private Consumer<? super ItemMeta> setLore(String[] lore) {
        return itemMeta -> itemMeta.setLore(Arrays.asList(lore));
    }

    @SuppressWarnings("deprecation")
    private Consumer<? super ItemMeta> appendLore(String[] lore) {
        return itemMeta -> {
            List<String> currentLore = itemMeta.getLore();
            currentLore.addAll(Arrays.asList(lore));
            itemMeta.setLore(currentLore);
        };
    }

    @Override
    public @Nullable List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) return Arrays.asList("set", "append");
        return Arrays.asList(args[args.length - 1] + "\\n", args[args.length - 1] + "\\t");
    }
}
