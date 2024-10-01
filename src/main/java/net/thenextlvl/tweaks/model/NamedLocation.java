package net.thenextlvl.tweaks.model;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;

@Getter
public class NamedLocation extends Location {
    private final String name;

    public NamedLocation(String name, World world, double x, double y, double z, float yaw, float pitch) {
        super(world, x, y, z, yaw, pitch);
        this.name = name;
    }

    public NamedLocation(String name, Location location) {
        this(name, location.getWorld(), location.x(), location.y(), location.z(), location.getYaw(), location.getPitch());
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
