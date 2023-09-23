package net.thenextlvl.tweaks.command.item;

import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.CommandSenderException;
import net.thenextlvl.tweaks.util.Messages;
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
public class LoreCommand implements TabExecutor {
    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) throw new CommandSenderException();
        if (args.length < 1) return false;

        var inventory = player.getInventory();
        var item = inventory.getItemInMainHand();

        if (item.getType().isEmpty()) {
            player.sendRichMessage(Messages.hold.item.message(player.locale(), player));
            return true;
        }

        if (args.length < 2) {
            player.sendRichMessage(Messages.item.lore.tip.message(player.locale(), sender));
        if (args.length < 2 && !args[0].equalsIgnoreCase("unset")) {
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
            player.sendRichMessage(Messages.item.lore.fail.message(player.locale(), player));
            return false;
        }

        inventory.setItemInMainHand(item);
        player.sendRichMessage(Messages.item.lore.success.message(player.locale(), player));
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
        if (args.length == 1) return Arrays.asList("set", "append");
        return Arrays.asList(args[args.length - 1] + "\\n", args[args.length - 1] + "\\t");
    }
}
