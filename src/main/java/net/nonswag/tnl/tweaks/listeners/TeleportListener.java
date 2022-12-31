package net.nonswag.tnl.tweaks.listeners;

import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.tweaks.api.manager.PositionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportListener implements Listener {

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.UNKNOWN)) return;
        TNLPlayer player = TNLPlayer.cast(event.getPlayer());
        player.getManager(PositionManager.class).setLastPosition(event.getFrom());
    }
}
