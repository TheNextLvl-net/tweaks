package net.thenextlvl.tweaks.controller;

import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

@RequiredArgsConstructor
public class BackController {
    private final Map<Player, BlockingDeque<Location>> positions = new WeakHashMap<>();
    private final TweaksPlugin plugin;

    public @Nullable Location peekFirst(Player player) {
        var deque = positions.get(player);
        if (deque == null) return null;
        deque.removeIf(location -> !location.isWorldLoaded());
        return deque.peekFirst();
    }

    public void offerFirst(Player player, Location location) {
        positions.computeIfAbsent(player, ignored ->
                new LinkedBlockingDeque<>(plugin.config().general().backBufferStackSize())
        ).offerFirst(location);
    }

    public void remove(Player player, Location location) {
        positions.computeIfPresent(player, (ignored, locations) ->
                locations.remove(location) && locations.isEmpty() ? null : locations);
    }

    public void remove(Player player) {
        positions.remove(player);
    }
}
