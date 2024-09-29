package net.thenextlvl.tweaks.listener;

import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

@RequiredArgsConstructor
public class RespawnListener implements Listener {
    private final TweaksPlugin plugin;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!event.getRespawnReason().equals(PlayerRespawnEvent.RespawnReason.DEATH)) return;
        var config = plugin.config().spawn();
        if (config.location() == null || !config.teleportOnRespawn()) return;
        if (!config.ignoreRespawnPosition() && (event.isAnchorSpawn() || event.isBedSpawn())) return;
        event.setRespawnLocation(config.location());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        var config = plugin.config().spawn();
        if (config.location() == null || !config.teleportOnRespawn()) return;
        if (config.ignoreRespawnPosition()) event.getPlayer().setRespawnLocation(null);
    }
}
