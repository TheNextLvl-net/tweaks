package net.thenextlvl.tweaks.controller;

import com.google.common.base.Preconditions;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jspecify.annotations.NullMarked;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@NullMarked
public final class TeleportController {
    private final Map<UUID, Location> teleports = new ConcurrentHashMap<>();
    private final TweaksPlugin plugin;

    public TeleportController(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public CompletableFuture<Boolean> teleport(Player player, Location location, PlayerTeleportEvent.TeleportCause cause) {
        var cooldown = plugin.config().teleport.cooldown;
        if (cooldown <= 0 || player.hasPermission("tweaks.teleport.cooldown.bypass"))
            return player.teleportAsync(location, cause);
        if (location.equals(teleports.put(player.getUniqueId(), location)))
            return CompletableFuture.failedFuture(new IllegalStateException());
        plugin.bundle().sendMessage(player, plugin.config().teleport.allowMovement
                        ? "command.teleport.cooldown"
                        : "command.teleport.cooldown.movement",
                Formatter.number("time", cooldown / 1000d));
        return scheduleTeleport(player, location, cause);
    }

    @SuppressWarnings("BusyWait")
    private CompletableFuture<Boolean> scheduleTeleport(Player player, Location location, PlayerTeleportEvent.TeleportCause cause) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                var previous = player.getLocation();
                var cooldown = plugin.config().teleport.cooldown;
                var teleportTime = System.currentTimeMillis() + cooldown;
                var blockMovements = !plugin.config().teleport.allowMovement;

                while (System.currentTimeMillis() < teleportTime) {
                    if (blockMovements && !player.getWorld().equals(previous.getWorld())) return false;
                    if (blockMovements && player.getLocation().distanceSquared(previous) > .2) return false;
                    Preconditions.checkState(player.isConnected());
                    Preconditions.checkState(location.equals(teleports.getOrDefault(player.getUniqueId(), location)));
                    Thread.sleep(Math.min(50, teleportTime - System.currentTimeMillis()));
                }

                return true;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                teleports.remove(player.getUniqueId());
            }
        }).thenCompose(success -> {
            if (success) return player.teleportAsync(location, cause);
            return CompletableFuture.completedFuture(false);
        });
    }

    public void remove(Player player) {
        teleports.remove(player.getUniqueId());
    }
}
