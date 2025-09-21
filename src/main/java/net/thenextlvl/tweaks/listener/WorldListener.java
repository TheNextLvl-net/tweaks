package net.thenextlvl.tweaks.listener;

import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class WorldListener implements Listener {
    private final TweaksPlugin plugin;

    public WorldListener(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWorldChanged(PlayerChangedWorldEvent event) {
        var permissionLevel = plugin.config().general.defaultPermissionLevel;
        if (permissionLevel != -1) event.getPlayer().sendOpLevel(permissionLevel);
    }
}
