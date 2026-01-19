package net.thenextlvl.tweaks.controller;

import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.model.NamedLocation;
import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@NullMarked
public final class WarpController {
    private final TweaksPlugin plugin;

    public WarpController(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public CompletableFuture<Optional<Location>> getWarp(String name) {
        return CompletableFuture.supplyAsync(() -> plugin.dataController().getWarp(name));
    }

    public CompletableFuture<Set<NamedLocation>> getWarps() {
        return CompletableFuture.supplyAsync(() -> plugin.dataController().getWarps());
    }

    public CompletableFuture<Boolean> deleteWarp(String name) {
        return CompletableFuture.supplyAsync(() -> plugin.dataController().deleteWarp(name));
    }

    public CompletableFuture<Boolean> setWarp(String name, Location location) {
        return CompletableFuture.supplyAsync(() -> plugin.dataController().setWarp(name, location));
    }
}
