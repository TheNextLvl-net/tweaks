package net.thenextlvl.tweaks.command.player;

import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.CommandSenderException;
import net.thenextlvl.tweaks.command.api.PlayerNotAffectedException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

@CommandInfo(
        name = "enderchest",
        usage = "/<command> (player)",
        description = "open your own or someone else's enderchest",
        permission = "tweaks.command.enderchest",
        aliases = {"ec"}
)
public class EnderChestCommand extends PlayerCommand implements Listener {
    private final Set<HumanEntity> viewers = Collections.newSetFromMap(new WeakHashMap<>());

    public EnderChestCommand(TweaksPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    protected void execute(CommandSender sender, Player target) {
        if (!(sender instanceof Player player)) throw new CommandSenderException();
        if (!isAllowed(sender, target)) throw new PlayerNotAffectedException(target);
        if (!player.equals(target)) viewers.add(player);
        player.openInventory(target.getEnderChest());
    }

    @Override
    protected @Nullable String getArgumentPermission(CommandSender sender, Player argument) {
        return sender.equals(argument) ? null : "tweaks.command.enderchest.others";
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        viewers.remove(event.getPlayer());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked().hasPermission("tweaks.command.enderchest.edit")) return;
        if (viewers.contains(event.getWhoClicked())) event.setCancelled(true);
    }
}
