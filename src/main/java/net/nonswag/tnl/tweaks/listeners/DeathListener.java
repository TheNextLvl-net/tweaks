package net.nonswag.tnl.tweaks.listeners;

import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.tweaks.commands.BackCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import javax.annotation.Nonnull;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(@Nonnull PlayerDeathEvent event) {
        TNLPlayer player = TNLPlayer.cast(event.getEntity());
        BackCommand.setLastPosition(player, player.getLocation());
    }
}
