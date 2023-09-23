package net.thenextlvl.tweaks.listener;

import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

@RequiredArgsConstructor
public class WorldListener implements Listener {
    private final TweaksPlugin plugin;

    @EventHandler
    public void onWorldChanged(PlayerChangedWorldEvent event) {
        var permissionLevel = plugin.config().generalConfig().defaultPermissionLevel();
        if (permissionLevel != -1) event.getPlayer().sendOpLevel(permissionLevel);
    }
}
