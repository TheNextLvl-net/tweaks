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
        name = "brewing-stand",
        description = "open a virtual brewing stand",
        permission = "tweaks.command.brewing-stand",
        aliases = "brewing"
)
public class BrewingStandCommand implements CommandExecutor {
    private final Map<Player, Inventory> inventories = new WeakHashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) throw new CommandSenderException();
        player.openInventory(inventories.computeIfAbsent(player, p ->
                Bukkit.createInventory(p, InventoryType.BREWING)));
        return true;
    }
}
