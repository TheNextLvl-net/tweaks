package net.nonswag.tnl.tweaks.api.manager;

import lombok.Getter;
import lombok.Setter;
import net.nonswag.tnl.listener.api.player.manager.Manager;
import org.bukkit.Location;

import javax.annotation.Nullable;

@Getter
@Setter
public abstract class PositionManager extends Manager {

    @Nullable
    private Location lastPosition;

}
