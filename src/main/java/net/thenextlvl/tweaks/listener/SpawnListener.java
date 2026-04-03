package net.thenextlvl.tweaks.listener;

import io.papermc.paper.event.player.AsyncPlayerSpawnLocationEvent;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class SpawnListener implements Listener {
    private final TweaksPlugin plugin;

    public SpawnListener(final TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerSpawnLocation(final AsyncPlayerSpawnLocationEvent event) {
        final var config = plugin.config().spawn;
        if (config.location == null || !config.location.isWorldLoaded()) return;
        if ((!config.teleportOnFirstJoin || !event.isNewPlayer()) 
            && (!config.teleportOnJoin || event.isNewPlayer())) return;
        event.setSpawnLocation(config.location);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerRespawn(final PlayerRespawnEvent event) {
        if (!event.getRespawnReason().equals(PlayerRespawnEvent.RespawnReason.DEATH)) return;
        final var config = plugin.config().spawn;
        if (config.location == null || !config.location.isWorldLoaded() || !config.teleportOnRespawn) return;
        if (!config.ignoreRespawnPosition && (event.isAnchorSpawn() || event.isBedSpawn())) return;
        event.setRespawnLocation(config.location);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeath(final PlayerDeathEvent event) {
        final var config = plugin.config().spawn;
        if (config.location == null || !config.location.isWorldLoaded() || !config.teleportOnRespawn) return;
        if (config.ignoreRespawnPosition) event.getPlayer().setRespawnLocation(null);
    }
}
