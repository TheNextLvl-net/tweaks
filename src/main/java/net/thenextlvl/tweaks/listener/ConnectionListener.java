package net.thenextlvl.tweaks.listener;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class ConnectionListener implements Listener {
    private final TweaksPlugin plugin;

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        var permissionLevel = plugin.config().general().defaultPermissionLevel();
        if (permissionLevel != -1) event.getPlayer().sendOpLevel(permissionLevel);
        if (!plugin.config().general().overrideJoinMessage()) return;
        plugin.getServer().getOnlinePlayers().forEach(player -> plugin.bundle().sendMessage(player, "player.connected",
                Placeholder.parsed("player", event.getPlayer().getName())));
        event.joinMessage(null);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!plugin.config().general().overrideQuitMessage()) return;
        plugin.getServer().getOnlinePlayers().forEach(player -> plugin.bundle().sendMessage(player, "player.disconnected",
                Placeholder.parsed("player", event.getPlayer().getName())));
        event.quitMessage(null);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void cleanup(PlayerQuitEvent event) {
        plugin.msgController().removeConversations(event.getPlayer());
        plugin.tpaController().removeRequests(event.getPlayer());
        plugin.teleportController().remove(event.getPlayer());
        plugin.backController().remove(event.getPlayer());
    }
}
