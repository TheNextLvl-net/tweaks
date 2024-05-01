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

        var inventory = player.getInventory();

        if (args.length == 0 && !inventory.getItemInMainHand().getType().isEmpty()) {
            var message = repair(inventory.getItemInMainHand()) ? "item.repaired.success" : "item.repaired.fail";

            plugin.bundle().sendMessage(player, message);
            return true;

        } else if (args.length == 0) {
            plugin.bundle().sendMessage(player, "hold.item");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("all")) {

            for (var item : inventory.getContents()) repair(item);

            plugin.bundle().sendMessage(player, "item.repaired.all");
            return true;
        }

        return false;
    }

    private boolean repair(@Nullable ItemStack item) {
        return item != null && item.editMeta(Damageable.class, damageable -> damageable.setDamage(0));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length <= 1 ? Collections.singletonList("all") : null;
    }
}
