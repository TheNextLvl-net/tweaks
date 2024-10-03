package net.thenextlvl.tweaks.command.player;

import com.mojang.brigadier.Command;
import core.paper.item.ItemBuilder;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.IntStream;

@SuppressWarnings("UnstableApiUsage")
public class InventoryCommand extends PlayerCommand implements Listener {
    private final Map<HumanEntity, Player> viewers = new WeakHashMap<>();
    private final Map<HumanEntity, Set<HumanEntity>> providers = new WeakHashMap<>();
    private final Map<HumanEntity, Inventory> inventories = new WeakHashMap<>();

    public InventoryCommand(TweaksPlugin plugin) {
        super(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        var updateTime = Math.max(1, plugin.config().guis().inventory().updateTime());
        plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(plugin, task ->
                providers.forEach((provider, viewers) -> {
                    var inventory = inventories.get(provider);
                    if (inventory != null) updateInventory(inventory, provider);
                }), updateTime, updateTime);
    }

    public void register(Commands registrar) {
        var command = create(plugin.commands().inventory().command(),
                "tweaks.command.inventory", "tweaks.command.inventory.others");
        registrar.register(command, "Open someone else's inventory",
                plugin.commands().inventory().aliases());
    }

    @Override
    protected int execute(CommandSender sender, Player target) {
        if (!(sender instanceof Player player)) {
            plugin.bundle().sendMessage(sender, "command.sender");
            return 0;
        }

        if (sender.equals(target)) {
            plugin.bundle().sendMessage(sender, "command.player.excluded",
                    Placeholder.parsed("player", sender.getName()));
            return 0;
        }

        var inventory = plugin.getServer().createInventory(target, 54, plugin.bundle().component(player,
                "gui.inventory.title", Placeholder.component("player", target.name())));
        updateInventory(inventory, target);
        addPlaceholders(inventory, player);
        player.openInventory(inventory);

        inventories.put(target, inventory);
        providers.computeIfAbsent(target, humanEntity ->
                        Collections.newSetFromMap(new WeakHashMap<>()))
                .add(player);
        viewers.put(player, target);
        return Command.SINGLE_SUCCESS;
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

    private void addPlaceholders(Inventory inventory, Player player) {
        var inventoryConfig = plugin.config().guis().inventory();
        var placeholder = new ItemBuilder(inventoryConfig.placeholder()).hideTooltip(true);
        inventory.setItem(36, new ItemBuilder(inventoryConfig.helmet())
                .itemName(plugin.bundle().component(player, "gui.placeholder.helmet")));
        inventory.setItem(37, new ItemBuilder(inventoryConfig.chestplate())
                .itemName(plugin.bundle().component(player, "gui.placeholder.chestplate")));
        inventory.setItem(38, new ItemBuilder(inventoryConfig.leggings())
                .itemName(plugin.bundle().component(player, "gui.placeholder.leggings")));
        inventory.setItem(39, new ItemBuilder(inventoryConfig.boots())
                .itemName(plugin.bundle().component(player, "gui.placeholder.boots")));
        inventory.setItem(41, new ItemBuilder(inventoryConfig.offHand())
                .itemName(plugin.bundle().component(player, "gui.placeholder.off-hand")));
        inventory.setItem(43, new ItemBuilder(inventoryConfig.cursor())
                .itemName(plugin.bundle().component(player, "gui.placeholder.cursor")));
        IntStream.of(40, 42, 44, 49, 51, 53).forEach(i -> inventory.setItem(i, placeholder));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent event) {
        var target = viewers.get(event.getPlayer());
        viewers.remove(event.getPlayer());
        if (providers.containsKey(target)) providers.get(target).remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProviderInventoryClick(InventoryClickEvent event) {
        onProviderInventoryAction(event.getWhoClicked());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProviderInventoryCreative(InventoryCreativeEvent event) {
        onProviderInventoryAction(event.getWhoClicked());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProviderInventoryDrag(InventoryDragEvent event) {
        onProviderInventoryAction(event.getWhoClicked());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProviderItemDrop(PlayerDropItemEvent event) {
        onProviderInventoryAction(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProviderItemPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof HumanEntity player) onProviderInventoryAction(player);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProviderArrowPickup(PlayerPickupArrowEvent event) {
        onProviderInventoryAction(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProviderSwapHandItems(PlayerSwapHandItemsEvent event) {
        onProviderInventoryAction(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProviderItemBreak(PlayerItemBreakEvent event) {
        onProviderInventoryAction(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProviderItemConsume(PlayerItemConsumeEvent event) {
        onProviderInventoryAction(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProviderItemDamage(PlayerItemDamageEvent event) {
        onProviderInventoryAction(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProviderItemMend(PlayerItemMendEvent event) {
        onProviderInventoryAction(event.getPlayer());
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
        var inventory = inventories.get(provider);
        provider.getScheduler().run(plugin, task -> providers.get(provider)
                        .forEach(player -> updateInventory(inventory, provider)),
                inventory::close);
    }

    public boolean onViewerInventoryAction(@Nullable Inventory clicked, InventoryView view, HumanEntity viewer, int slot) {
        if (!viewers.containsKey(viewer)) return false;
        if (!viewer.hasPermission("tweaks.command.inventory.edit")) return true;
        var target = viewers.get(viewer);
        target.getScheduler().run(plugin, task -> {
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
        }, null);
        return view.getTopInventory().equals(clicked)
               && ((slot >= 36 && slot <= 44)
                   || slot == 49
                   || slot == 51
                   || slot == 53);
    }
}
