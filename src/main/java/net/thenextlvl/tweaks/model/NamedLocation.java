package net.thenextlvl.tweaks.model;

import org.bukkit.Location;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@NullMarked
public final class NamedLocation extends Location {
    private final String name;

    public NamedLocation(final String name, final World world, final double x, final double y, final double z, final float yaw, final float pitch) {
        super(world, x, y, z, yaw, pitch);
        this.name = name;
    }

    public NamedLocation(final String name, final Location location) {
        this(name, location.getWorld(), location.x(), location.y(), location.z(), location.getYaw(), location.getPitch());
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        final NamedLocation that = (NamedLocation) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
