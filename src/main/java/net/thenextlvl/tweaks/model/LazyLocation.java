package net.thenextlvl.tweaks.model;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class LazyLocation extends Location implements Keyed {
    private Key key;

    public LazyLocation(final Location location) {
        super(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        this.key = location.getWorld().key();
    }

    public LazyLocation(final Key key, final double x, final double y, final double z, final float yaw, final float pitch) {
        super(Bukkit.getWorld(key), x, y, z, yaw, pitch);
        this.key = key;
    }

    @Override
    public void setWorld(@Nullable final World world) {
        super.setWorld(world);
        if (world != null) this.key = world.key();
    }

    @Override
    public @Nullable World getWorld() {
        var world = super.getWorld();
        if (world != null) return world;

        world = Bukkit.getWorld(key);
        if (world != null) setWorld(world);
        return world;
    }

    @Override
    public boolean isWorldLoaded() {
        return getWorld() != null;
    }

    @Override
    public Key key() {
        return key;
    }
}
