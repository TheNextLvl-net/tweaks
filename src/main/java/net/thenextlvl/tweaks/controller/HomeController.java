package net.thenextlvl.tweaks.controller;

import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.model.NamedLocation;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class HomeController {
    private final TweaksPlugin plugin;

    public CompletableFuture<@Nullable Location> getHome(OfflinePlayer player, String name) {
        return CompletableFuture.supplyAsync(() -> plugin.dataController().getHome(player, name))
                .exceptionally(throwable -> null);
    }

    public CompletableFuture<@Unmodifiable Set<NamedLocation>> getHomes(OfflinePlayer player) {
        return CompletableFuture.supplyAsync(() -> plugin.dataController().getHomes(player))
                .exceptionally(throwable -> Set.of());
    }

    public CompletableFuture<Boolean> deleteHome(OfflinePlayer player, String name) {
        return CompletableFuture.supplyAsync(() -> plugin.dataController().deleteHome(player, name))
                .exceptionally(throwable -> false);
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
                .map(ServiceController::getPermissions)
                .flatMap(controller -> controller.getPermissionHolder(player))
                .flatMap(permissionHolder -> permissionHolder.intInfoNode("max-homes"))
                .orElse(plugin.config().homes().limit());
    }
}
