package net.thenextlvl.tweaks.command.player;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

@SuppressWarnings("UnstableApiUsage")
public class EnderChestCommand extends PlayerCommand implements Listener {
    private final Set<HumanEntity> viewers = Collections.newSetFromMap(new WeakHashMap<>());

    public EnderChestCommand(TweaksPlugin plugin) {
        super(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void register(Commands registrar) {
        var command = create(plugin.commands().enderchest().command(),
                "tweaks.command.enderchest", "tweaks.command.enderchest.others");
        registrar.register(command, "Open your own or someone else's enderchest",
                plugin.commands().enderchest().aliases());
    }

    @Override
    protected int execute(CommandSender sender, Player target) {
        if (!(sender instanceof Player player)) {
            plugin.bundle().sendMessage(sender, "command.sender");
            return 0;
        }

        if (!player.equals(target)) viewers.add(player);
        // todo: custom gui + offline mode
        player.openInventory(target.getEnderChest());
        return Command.SINGLE_SUCCESS;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent event) {
        viewers.remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked().hasPermission("tweaks.command.enderchest.edit")) return;
        if (viewers.contains(event.getWhoClicked())) event.setCancelled(true);
    }
}
