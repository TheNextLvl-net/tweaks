package net.thenextlvl.tweaks.command.item;

import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.CommandSenderException;
import net.thenextlvl.tweaks.command.api.NoPermissionException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

@CommandInfo(
        name = "repair",
        usage = "/<command> (all)",
        description = "repair your tools",
        permission = "tweaks.command.repair"
)
public class RepairCommand implements TabExecutor {

    private static final String REPAIR_ALL_PERMISSION = "tweaks.command.repair.all";

    public RepairCommand() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        if (pluginManager.getPermission(REPAIR_ALL_PERMISSION) == null)
            pluginManager.addPermission(new Permission(REPAIR_ALL_PERMISSION));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) throw new CommandSenderException();

        PlayerInventory inventory = player.getInventory();
        if (args.length == 0) {
            inventory.setItemInMainHand(repairItem(inventory.getItemInMainHand()));
            // TODO: Repaired the item in hand
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("hand"))
            return onCommand(sender, command, label, new String[0]);

        if (args.length == 1 && args[0].equalsIgnoreCase("all")) {
            if (!sender.hasPermission(REPAIR_ALL_PERMISSION)) throw new NoPermissionException(REPAIR_ALL_PERMISSION);

            @Nullable ItemStack[] contents = inventory.getContents();
            for (int i = 0; i < contents.length; i++) {
                contents[i] = repairItem(contents[i]);
            }
            inventory.setContents(contents);

            @Nullable ItemStack[] armor = inventory.getArmorContents();
            for (int i = 0; i < armor.length; i++) {
                armor[i] = repairItem(armor[i]);
            }
            inventory.setArmorContents(armor);
            // TODO: All items in your inventory were repaired.
            return true;
        }

        return false;
    }

    private @Nullable ItemStack repairItem(@Nullable ItemStack item) {
        if (item == null) return null;
        item.editMeta(Damageable.class, damageable -> damageable.setDamage(0));
        return item;
    }

    @Override
    public @Nullable List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1 || !sender.hasPermission(REPAIR_ALL_PERMISSION)) return Collections.emptyList();
        return Collections.singletonList("all");
    }
}
