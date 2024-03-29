package net.thenextlvl.tweaks.command.item;

import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.CommandSenderException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

@CommandInfo(
        name = "repair",
        usage = "/<command> (all)",
        description = "repair your tools",
        permission = "tweaks.command.repair"
)
@RequiredArgsConstructor
public class RepairCommand implements TabExecutor {
    private final TweaksPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) throw new CommandSenderException();

        PlayerInventory inventory = player.getInventory();
        if (args.length == 0 && !inventory.getItemInMainHand().getType().isEmpty()) {
            inventory.setItemInMainHand(repairItem(inventory.getItemInMainHand()));
            plugin.bundle().sendMessage(player, "item.repaired");
            return true;
        } else if (args.length == 0) {
            plugin.bundle().sendMessage(player, "hold.item");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("all")) {

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
            plugin.bundle().sendMessage(player, "item.repaired.all");
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
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length <= 1 ? Collections.singletonList("all") : null;
    }
}
