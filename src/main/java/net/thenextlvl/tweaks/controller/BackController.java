package net.thenextlvl.tweaks.controller;

import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

@NullMarked
public final class BackController {
    private final Map<UUID, BlockingDeque<Location>> positions = new ConcurrentHashMap<>();
    private final Map<UUID, Location> positionLock = new ConcurrentHashMap<>();
    private final Map<UUID, Location> lastSafeLocation = new ConcurrentHashMap<>();
    private final TweaksPlugin plugin;

    public BackController(final TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public @Nullable Location peekFirst(final Player player) {
        final var deque = positions.get(player.getUniqueId());
        if (deque == null) return null;
        deque.removeIf(location -> !location.isWorldLoaded());
        return deque.peekFirst();
    }
    
    public void setLastSafeLocation(final Player player, final Location location) {
        lastSafeLocation.put(player.getUniqueId(), location);
    }
    
    public @Nullable Location getLastSafeLocation(final Player player) {
        return lastSafeLocation.get(player.getUniqueId());
    }

    public void offerFirst(final Player player, final Location location) {
        positions.computeIfAbsent(player.getUniqueId(), ignored -> {
            return new LinkedBlockingDeque<>(plugin.config().general.backBufferStackSize);
        }).offerFirst(location);
    }

    public void remove(final Player player, final Location location) {
        positions.computeIfPresent(player.getUniqueId(), (ignored, locations) ->
                locations.remove(location) && locations.isEmpty() ? null : locations);
    }

    public void remove(final Player player) {
        positionLock.remove(player.getUniqueId());
        positions.remove(player.getUniqueId());
    }

    public @Nullable Location getLock(final Player player) {
        return positionLock.get(player.getUniqueId());
    }

    public void lock(final Player player, final Location location) {
        positionLock.put(player.getUniqueId(), location);
    }

    public void unlock(final Player player) {
        positionLock.remove(player.getUniqueId());
    }
}
