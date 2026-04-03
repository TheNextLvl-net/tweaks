package net.thenextlvl.tweaks.command.player;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.translation.Argument;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.Material;
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
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.stream.IntStream;

@NullMarked
public class InventoryCommand extends PlayerCommand implements Listener {
    private final Map<HumanEntity, Player> viewers = new WeakHashMap<>();
    private final Map<HumanEntity, ViewableInventory> viewables = new WeakHashMap<>();

    public InventoryCommand(final TweaksPlugin plugin) {
        super(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        //plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(plugin, task ->
        //        viewables.values().forEach(ViewableInventory::updateInventory), 5, 5);
    }

    public void register(final Commands registrar) {
        final var command = create(plugin.commands().inventory.command,
                "tweaks.command.inventory", "tweaks.command.inventory");
        registrar.register(command, "Open someone else's inventory",
                plugin.commands().inventory.aliases);
    }

    @Override
    protected int execute(final CommandSender sender, final Player target) {
        if (!(sender instanceof final Player player)) {
            plugin.bundle().sendMessage(sender, "command.sender");
            return 0;
        }

        if (sender.equals(target)) {
            plugin.bundle().sendMessage(sender, "command.player.excluded",
                    Placeholder.parsed("player", sender.getName()));
            return 0;
        }

        // player.openInventory(target.getInventory());

        final var viewable = new ViewableInventory(player, target);
        player.openInventory(viewable.getInventory());
        viewables.put(target, viewable);
        viewers.put(player, target);
        return Command.SINGLE_SUCCESS;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClose(final InventoryCloseEvent event) {
        viewables.remove(viewers.remove(event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProviderInventoryClick(final InventoryClickEvent event) {
        onProviderInventoryAction(event.getWhoClicked());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProviderInventoryCreative(final InventoryCreativeEvent event) {
        onProviderInventoryAction(event.getWhoClicked());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProviderInventoryDrag(final InventoryDragEvent event) {
        onProviderInventoryAction(event.getWhoClicked());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProviderItemDrop(final PlayerDropItemEvent event) {
        onProviderInventoryAction(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProviderItemPickup(final EntityPickupItemEvent event) {
        if (event.getEntity() instanceof final HumanEntity player) onProviderInventoryAction(player);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProviderArrowPickup(final PlayerPickupArrowEvent event) {
        onProviderInventoryAction(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProviderSwapHandItems(final PlayerSwapHandItemsEvent event) {
        onProviderInventoryAction(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProviderItemBreak(final PlayerItemBreakEvent event) {
        onProviderInventoryAction(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProviderItemConsume(final PlayerItemConsumeEvent event) {
        onProviderInventoryAction(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProviderItemDamage(final PlayerItemDamageEvent event) {
        onProviderInventoryAction(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProviderItemMend(final PlayerItemMendEvent event) {
        onProviderInventoryAction(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onViewerInventoryClick(final InventoryClickEvent event) {
        event.setCancelled(onViewerInventoryAction(
                event.getClickedInventory(),
                event.getView(),
                event.getWhoClicked(),
                event.getSlot()
        ));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onViewerInventoryDrag(final InventoryDragEvent event) {
        onViewerInventoryAction(event.getInventory(), event.getView(), event.getWhoClicked(), -1);
    }

    public void onProviderInventoryAction(final HumanEntity provider) {
        final var inventory = viewables.get(provider);
        if (inventory == null) return;
        provider.getScheduler().run(plugin, task -> inventory.updateInventory(), inventory::close);
    }

    public boolean onViewerInventoryAction(@Nullable final Inventory clicked, final InventoryView view, final HumanEntity viewer, final int slot) {
        final var target = viewers.get(viewer);
        if (target == null) return false;
        if (!viewer.hasPermission("tweaks.command.inventory.edit")) return true;
        target.getScheduler().run(plugin, task -> {
            IntStream.range(0, 36).forEach(i -> {
                final var content = view.getTopInventory().getContents()[i];
                target.getInventory().setItem(i, content);
            });
            target.getInventory().setHelmet(view.getTopInventory().getItem(45));
            target.getInventory().setChestplate(view.getTopInventory().getItem(46));
            target.getInventory().setLeggings(view.getTopInventory().getItem(47));
            target.getInventory().setBoots(view.getTopInventory().getItem(48));
            target.getInventory().setItemInOffHand(view.getTopInventory().getItem(50));
            target.setItemOnCursor(view.getTopInventory().getItem(52));
        }, viewer::closeInventory);
        return view.getTopInventory().equals(clicked)
               && ((slot >= 36 && slot <= 44)
                   || slot == 49
                   || slot == 51
                   || slot == 53);
    }

    private class ViewableInventory implements InventoryHolder {
        private final Player target;
        private final Inventory inventory;

        private ViewableInventory(final Player viewer, final Player target) {
            final var title = plugin.bundle().component("gui.inventory.title", viewer,
                    Argument.component("player", target.name()));
            this.inventory = plugin.getServer().createInventory(this, 54, title);
            this.target = target;
            addPlaceholders(viewer);
            updateInventory();
        }

        @Override
        public Inventory getInventory() {
            return inventory;
        }

        private void updateInventory() {
            final var inventory = target.getInventory();
            for (var i = 0; i < inventory.getStorageContents().length; i++)
                this.inventory.setItem(i, inventory.getStorageContents()[i]);
            this.inventory.setItem(45, inventory.getHelmet());
            this.inventory.setItem(46, inventory.getChestplate());
            this.inventory.setItem(47, inventory.getLeggings());
            this.inventory.setItem(48, inventory.getBoots());
            this.inventory.setItem(50, inventory.getItemInOffHand());
            this.inventory.setItem(52, target.getItemOnCursor());
        }

        private void addPlaceholders(final Player viewer) {
            final var inventoryConfig = plugin.config().guis.inventory;
            final var placeholder = ItemStack.of(inventoryConfig.placeholder);
            placeholder.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().hideTooltip(true).build());
            inventory.setItem(36, withName(inventoryConfig.helmet, "gui.placeholder.helmet", viewer));
            inventory.setItem(37, withName(inventoryConfig.chestplate, "gui.placeholder.chestplate", viewer));
            inventory.setItem(38, withName(inventoryConfig.leggings, "gui.placeholder.leggings", viewer));
            inventory.setItem(39, withName(inventoryConfig.boots, "gui.placeholder.boots", viewer));
            inventory.setItem(41, withName(inventoryConfig.offHand, "gui.placeholder.off-hand", viewer));
            inventory.setItem(43, withName(inventoryConfig.cursor, "gui.placeholder.cursor", viewer));
            IntStream.of(40, 42, 44, 49, 51, 53).forEach(i -> inventory.setItem(i, placeholder));
        }

        public void close() {
            inventory.close();
        }
    }

    private ItemStack withName(final Material material, final String name, final Audience viewer) {
        final var itemStack = ItemStack.of(material);
        itemStack.setData(DataComponentTypes.ITEM_NAME, plugin.bundle().component(name, viewer));
        return itemStack;
    }
}
