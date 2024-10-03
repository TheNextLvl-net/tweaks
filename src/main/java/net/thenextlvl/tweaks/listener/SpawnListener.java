package net.thenextlvl.tweaks.listener;

import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

@RequiredArgsConstructor
public class SpawnListener implements Listener {
    private final TweaksPlugin plugin;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerSpawnLocation(PlayerSpawnLocationEvent event) {
        var config = plugin.config().spawn();
        if (config.location() == null) return;
        if ((!config.teleportOnFirstJoin() || event.getPlayer().hasPlayedBefore())
            && (!config.teleportOnJoin() || !event.getPlayer().hasPlayedBefore())) return;
        event.setSpawnLocation(config.location());
    }

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