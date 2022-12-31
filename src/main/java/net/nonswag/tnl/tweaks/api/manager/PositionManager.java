package net.nonswag.tnl.tweaks.api.manager;

import lombok.Getter;
import lombok.Setter;
import net.nonswag.core.api.annotation.FieldsAreNullableByDefault;
import net.nonswag.tnl.listener.api.player.manager.Manager;
import org.bukkit.Location;

@Getter
@Setter
@FieldsAreNullableByDefault
public abstract class PositionManager extends Manager {
    private Location lastPosition;
}
