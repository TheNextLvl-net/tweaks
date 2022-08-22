package net.nonswag.tnl.tweaks.listeners;

import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.tweaks.commands.BackCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import javax.annotation.Nonnull;

public class TeleportListener implements Listener {

    @EventHandler
    public void onTeleport(@Nonnull PlayerTeleportEvent event) {
        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.UNKNOWN)) return;
        TNLPlayer player = TNLPlayer.cast(event.getPlayer());
        BackCommand.setLastPosition(player, event.getFrom());
    }
}
