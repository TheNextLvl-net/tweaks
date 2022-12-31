package net.nonswag.tnl.tweaks.listeners;

import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.tweaks.api.manager.PositionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        TNLPlayer player = TNLPlayer.cast(event.getEntity());
        player.getManager(PositionManager.class).setLastPosition(player.worldManager().getLocation());
    }
}
