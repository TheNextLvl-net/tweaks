package net.thenextlvl.tweaks.controller;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class TeleportController {
    private final Map<Player, Location> teleports = new WeakHashMap<>();
    private final TweaksPlugin plugin;

    public CompletableFuture<Boolean> teleport(Player player, Location location, PlayerTeleportEvent.TeleportCause cause) {
        var cooldown = plugin.config().teleport().cooldown();
        if (cooldown <= 0) return player.teleportAsync(location, cause);
        if (location.equals(teleports.put(player, location)))
            return CompletableFuture.failedFuture(new IllegalStateException());
        var formatter = DecimalFormat.getInstance(player.locale());
        plugin.bundle().sendMessage(player, plugin.config().teleport().allowMovement()
                        ? "command.teleport.cooldown"
                        : "command.teleport.cooldown.movement",
                Placeholder.parsed("time", formatter.format(cooldown / 1000d)));
        return scheduleTeleport(player, location, cause);
    }

    @SuppressWarnings("BusyWait")
    private CompletableFuture<Boolean> scheduleTeleport(Player player, Location location, PlayerTeleportEvent.TeleportCause cause) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                var previous = player.getLocation();
                var cooldown = plugin.config().teleport().cooldown();
                var teleportTime = System.currentTimeMillis() + cooldown;
                var blockMovements = !plugin.config().teleport().allowMovement();

                while (System.currentTimeMillis() < teleportTime) {
                    if (blockMovements && !player.getWorld().equals(previous.getWorld())) return false;
                    if (blockMovements && player.getLocation().distanceSquared(previous) > .2) return false;
                    Preconditions.checkState(player.isConnected());
                    Preconditions.checkState(location.equals(teleports.getOrDefault(player, location)));
                    Thread.sleep(Math.min(50, teleportTime - System.currentTimeMillis()));
                }

                return true;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                teleports.remove(player);
            }
        }).thenCompose(success -> {
            if (success) return player.teleportAsync(location, cause);
            return CompletableFuture.completedFuture(false);
        });
    }

    public void remove(Player player) {
        teleports.remove(player);
    }
}
