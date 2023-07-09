package net.thenextlvl.tweaks.command.player;

import net.kyori.adventure.text.Component;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.CommandSenderException;
import net.thenextlvl.tweaks.command.api.PlayerNotAffectedException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@CommandInfo(
        name = "inventory",
        usage = "/<command> (player)",
        description = "open your own or someone else's inventory",
        permission = "tweaks.command.inventory",
        aliases = {"inv", "invsee"}
)
public class InventoryCommand extends PlayerCommand implements Listener {
    private final Map<HumanEntity, Player> viewers = new WeakHashMap<>();
    private final Map<HumanEntity, Set<HumanEntity>> providers = new WeakHashMap<>();
    private final Map<HumanEntity, Inventory> inventories = new WeakHashMap<>();
    private final TweaksPlugin plugin;

    public InventoryCommand(TweaksPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        var updateTime = Math.max(50, plugin.getTweaksConfig().inventoryConfig().updateTime());
        plugin.getFoliaLib().getImpl().runTimer(() -> providers.forEach((provider, viewers) -> {
            var inventory = inventories.get(provider);
            if (inventory != null) updateInventory(inventory, provider);
        }), updateTime, updateTime, TimeUnit.MILLISECONDS);
        this.plugin = plugin;
    }

    @Override
    protected boolean isAllowed(CommandSender sender, Player argument) {
        return !sender.equals(argument);
    }

    @Override
    protected void execute(CommandSender sender, Player target) {
        if (!(sender instanceof Player player)) throw new CommandSenderException();
        if (!isAllowed(sender, target)) throw new PlayerNotAffectedException(target);
        var inventory = Bukkit.createInventory(target, 54, Component.text(target.getName()));
        updateInventory(inventory, target);
        addPlaceholders(inventory);
        player.openInventory(inventory);

        inventories.put(target, inventory);
        providers.computeIfAbsent(target, humanEntity ->
                        Collections.newSetFromMap(new WeakHashMap<>()))
                .add(player);
        viewers.put(player, target);
    }

    private void updateInventory(Inventory inventory, HumanEntity target) {
        var targetInventory = target.getInventory();
        for (var i = 0; i < targetInventory.getStorageContents().length; i++)
            inventory.setItem(i, targetInventory.getStorageContents()[i]);
        inventory.setItem(45, targetInventory.getHelmet());
        inventory.setItem(46, targetInventory.getChestplate());
        inventory.setItem(47, targetInventory.getLeggings());
        inventory.setItem(48, targetInventory.getBoots());
        inventory.setItem(50, targetInventory.getItemInOffHand());
        inventory.setItem(52, target.getItemOnCursor());
    }

    private void addPlaceholders(Inventory inventory) {
        var inventoryConfig = plugin.getTweaksConfig().inventoryConfig();
        var placeholder = inventoryConfig.placeholder().serialize();
        inventory.setItem(36, inventoryConfig.helmet().serialize());
        inventory.setItem(37, inventoryConfig.chestplate().serialize());
        inventory.setItem(38, inventoryConfig.leggings().serialize());
        inventory.setItem(39, inventoryConfig.boots().serialize());
        inventory.setItem(41, inventoryConfig.offHand().serialize());
        inventory.setItem(43, inventoryConfig.cursor().serialize());
        IntStream.of(40, 42, 44, 49, 51, 53).forEach(i -> inventory.setItem(i, placeholder));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent event) {
        var target = viewers.get(event.getPlayer());
        viewers.remove(event.getPlayer());
        if (providers.containsKey(target)) providers.get(target).remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onProviderInventoryClick(InventoryClickEvent event) {
        onProviderInventoryAction(event.getWhoClicked());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onProviderInventoryDrag(InventoryDragEvent event) {
        onProviderInventoryAction(event.getWhoClicked());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onViewerInventoryClick(InventoryClickEvent event) {
        event.setCancelled(onViewerInventoryAction(
                event.getClickedInventory(),
                event.getView(),
                event.getWhoClicked(),
                event.getSlot()
        ));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onViewerInventoryDrag(InventoryDragEvent event) {
        onViewerInventoryAction(event.getInventory(), event.getView(), event.getWhoClicked(), -1);
    }

    public void onProviderInventoryAction(HumanEntity provider) {
        if (!providers.containsKey(provider)) return;
        if (!inventories.containsKey(provider)) return;
        var viewers = providers.get(provider);
        var inventory = inventories.get(provider);
        plugin.getFoliaLib().getImpl().runNextTick(() ->
                viewers.forEach(player -> updateInventory(inventory, provider))
        );
    }

    public boolean onViewerInventoryAction(@Nullable Inventory clicked, InventoryView view, HumanEntity viewer, int slot) {
        if (!viewers.containsKey(viewer)) return false;
        if (viewer.hasPermission("tweaks.command.inventory.edit")) {
            var target = viewers.get(viewer);
            plugin.getFoliaLib().getImpl().runNextTick(() -> {
                IntStream.range(0, 36).forEach(i -> {
                    var content = view.getTopInventory().getContents()[i];
                    target.getInventory().setItem(i, content);
                });
                target.getInventory().setHelmet(view.getTopInventory().getItem(45));
                target.getInventory().setChestplate(view.getTopInventory().getItem(46));
                target.getInventory().setLeggings(view.getTopInventory().getItem(47));
                target.getInventory().setBoots(view.getTopInventory().getItem(48));
                target.getInventory().setItemInOffHand(view.getTopInventory().getItem(50));
                target.setItemOnCursor(view.getTopInventory().getItem(52));
            });
            return view.getTopInventory().equals(clicked)
                    && ((slot >= 36 && slot <= 44)
                    || slot == 49
                    || slot == 51
                    || slot == 53);
        } else return true;
    }
}
