package net.thenextlvl.tweaks.listener;

import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.MushroomCow;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SheepRegrowWoolEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NullMarked;

import static org.bukkit.GameMode.ADVENTURE;
import static org.bukkit.GameMode.SURVIVAL;

@NullMarked
public final class EntityListener implements Listener {
    private final TweaksPlugin plugin;

    private final NamespacedKey mushroomCowStewCooldown = new NamespacedKey("tweaks", "mushroom_cow_stew_cooldown");
    private final NamespacedKey woolRegrowCooldown = new NamespacedKey("tweaks", "wool_regrow_cooldown");
    private final NamespacedKey cowMilkCooldown = new NamespacedKey("tweaks", "cow_milk_cooldown");

    public EntityListener(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAnimalBreed(PlayerInteractEntityEvent event) {
        if (!plugin.config().vanilla.animalHealByFeeding) return;
        if (!(event.getRightClicked() instanceof Animals animals)) return;
        var item = event.getPlayer().getInventory().getItem(event.getHand());
        if (!animals.isBreedItem(item) || animals.isDead()) return;
        var attribute = animals.getAttribute(Attribute.MAX_HEALTH);
        if (attribute == null || animals.getHealth() == attribute.getValue()) return;
        animals.setHealth(Math.min(animals.getHealth() + 1, attribute.getValue()));
        var gameMode = event.getPlayer().getGameMode();
        if (gameMode.equals(SURVIVAL) || gameMode.equals(ADVENTURE)) item.subtract();
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSheepWoolRegrow(SheepRegrowWoolEvent event) {
        var cooldown = plugin.config().vanilla.sheepWoolGrowthCooldown;
        if (cooldown > 0) cooldown(event, event.getEntity(), woolRegrowCooldown, cooldown);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCowMilk(PlayerInteractEntityEvent event) {
        var item = event.getPlayer().getInventory().getItem(event.getHand());
        if (!item.getType().equals(Material.BUCKET)) return;
        if (!(event.getRightClicked() instanceof Cow cow)) return;
        var cooldown = plugin.config().vanilla.cowMilkingCooldown;
        if (cooldown > 0) cooldown(event, cow, cowMilkCooldown, cooldown);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMushroomStew(PlayerInteractEntityEvent event) {
        var item = event.getPlayer().getInventory().getItem(event.getHand());
        if (!item.getType().equals(Material.BOWL)) return;
        if (!(event.getRightClicked() instanceof MushroomCow cow)) return;
        var cooldown = plugin.config().vanilla.mushroomStewCooldown;
        if (cooldown > 0) cooldown(event, cow, mushroomCowStewCooldown, cooldown);
    }

    private void cooldown(Cancellable cancellable, Entity entity, NamespacedKey key, long cooldown) {
        var now = System.currentTimeMillis();
        var container = entity.getPersistentDataContainer();
        var data = container.get(key, PersistentDataType.LONG);
        if (data != null && now - data < cooldown) cancellable.setCancelled(true);
        else container.set(key, PersistentDataType.LONG, now);
    }
}
