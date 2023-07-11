package net.thenextlvl.tweaks.command.workstation;

import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.CommandSenderException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Map;
import java.util.WeakHashMap;

@CommandInfo(
        name = "smoker",
        description = "open a virtual smoker",
        permission = "tweaks.command.smoker"
)
public class SmokerCommand implements CommandExecutor {
    private final Map<Player, Inventory> inventories = new WeakHashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) throw new CommandSenderException();
        player.openInventory(inventories.computeIfAbsent(player, p ->
                Bukkit.createInventory(p, InventoryType.SMOKER)));
        return true;
    }
}
