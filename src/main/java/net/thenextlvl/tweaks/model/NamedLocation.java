package net.thenextlvl.tweaks.model;

import org.bukkit.Location;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public class NamedLocation extends Location {
    private final String name;

    public NamedLocation(String name, World world, double x, double y, double z, float yaw, float pitch) {
        super(world, x, y, z, yaw, pitch);
        this.name = name;
    }

    public NamedLocation(String name, Location location) {
        this(name, location.getWorld(), location.x(), location.y(), location.z(), location.getYaw(), location.getPitch());
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        NamedLocation that = (NamedLocation) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
