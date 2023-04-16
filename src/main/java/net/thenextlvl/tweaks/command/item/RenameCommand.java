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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

@CommandInfo(name = "rename", usage = "/<command> [text...]", permission = "tweaks.command.rename", description = "Changes the display name of the item in your hand")
public class RenameCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player))
            throw new CommandSenderException();
        if (args.length < 1)
            return false;

        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItemInMainHand();

        String name = ChatColor.translateAlternateColorCodes('&', String.join(" ", args))
                .replace("\\t", "   ");

        if (!item.editMeta(itemMeta -> itemMeta.setDisplayName(name))) {
            // TODO: Something went wrong during updating the display name of the item in your hand.
            return false;
        }

        inventory.setItemInMainHand(item);
        // TODO: The display name of the item in your hand was successfully updated.
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
