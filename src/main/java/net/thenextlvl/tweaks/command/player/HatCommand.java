package net.thenextlvl.tweaks.command.player;

import net.thenextlvl.tweaks.command.api.CommandInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

@CommandInfo(name = "hat", permission = "tweaks.command.hat")
public class HatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            // TODO: You have to be on the server
            return true;
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItemInMainHand();

        ItemStack helmet = inventory.getHelmet();
        inventory.setItemInMainHand(helmet);
        inventory.setHelmet(item);

        // TODO: Your hat was set to ... .
        return true;
    }
}
