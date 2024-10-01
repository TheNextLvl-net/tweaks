package net.thenextlvl.tweaks.listener;

import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.COMMAND;

@RequiredArgsConstructor
public class BackListener implements Listener {
    private final TweaksPlugin plugin;

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        if (switch (event.getCause()) { // todo: config entry
            case ENDER_PEARL, COMMAND, PLUGIN, END_PORTAL, SPECTATE, UNKNOWN -> false;
            case NETHER_PORTAL, END_GATEWAY, CHORUS_FRUIT, DISMOUNT, EXIT_BED -> true;
        }) return;

        var player = event.getPlayer();

        if (event.getCause().equals(COMMAND) && event.getTo().equals(plugin.backController().getLock(player))) {
            plugin.backController().unlock(player);
            return;
        }

        if (!player.hasPermission("tweaks.command.back")) return;

        plugin.backController().offerFirst(player, event.getFrom());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent event) {
        var player = event.getPlayer();

        if (player.getLocation().getY() < player.getWorld().getMinHeight()) return;
        if (!player.hasPermission("tweaks.command.back")) return;

        plugin.backController().offerFirst(player, player.getLocation());
    }
}
