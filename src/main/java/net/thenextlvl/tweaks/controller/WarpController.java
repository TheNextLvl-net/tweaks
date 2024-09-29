package net.thenextlvl.tweaks.controller;

import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.model.NamedLocation;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class WarpController {
    private final TweaksPlugin plugin;

    public CompletableFuture<@Nullable Location> getWarp(String name) {
        return CompletableFuture.supplyAsync(() -> plugin.dataController().getWarp(name))
                .exceptionally(throwable -> null);
    }

    public CompletableFuture<@Unmodifiable Set<NamedLocation>> getWarps() {
        return CompletableFuture.supplyAsync(() -> plugin.dataController().getWarps())
                .exceptionally(throwable -> Set.of());
    }

    public CompletableFuture<Boolean> deleteWarp(String name) {
        return CompletableFuture.supplyAsync(() -> plugin.dataController().deleteWarp(name))
                .exceptionally(throwable -> false);
    }

    public CompletableFuture<Void> setWarp(String name, Location location) {
        return CompletableFuture.runAsync(() -> plugin.dataController().setWarp(name, location));
    }
}