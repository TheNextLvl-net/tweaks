package net.thenextlvl.tweaks.controller;

import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.model.NamedLocation;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@NullMarked
public final class HomeController {
    private final TweaksPlugin plugin;

    public HomeController(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public CompletableFuture<Optional<Location>> getHome(OfflinePlayer player, String name) {
        return CompletableFuture.supplyAsync(() -> plugin.dataController().getHome(player, name));
    }

    public CompletableFuture<Set<NamedLocation>> getHomes(OfflinePlayer player) {
        return CompletableFuture.supplyAsync(() -> plugin.dataController().getHomes(player));
    }

    public CompletableFuture<Boolean> deleteHome(OfflinePlayer player, String name) {
        return CompletableFuture.supplyAsync(() -> plugin.dataController().deleteHome(player, name));
    }

    /**
     * Retrieves the maximum number of homes that the specified player can set.
     *
     * @param player the player whose maximum home count is being queried
     * @return the maximum number of homes the player is allowed to set;
     * if not explicitly defined in permissions, the default limit
     * from the plugin configuration is returned;
     */
    public int getMaxHomeCount(Player player) {
        return Optional.ofNullable(plugin.serviceController())
                .flatMap(controller -> controller.getMaxHomeCount(player))
                .orElse(plugin.config().homes.limit);
    }
}
