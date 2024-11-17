package net.thenextlvl.tweaks.listener;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
@RequiredArgsConstructor
public class ConnectionListener implements Listener {
    private final TweaksPlugin plugin;

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        var permissionLevel = plugin.config().general().defaultPermissionLevel();
        if (permissionLevel != -1) event.getPlayer().sendOpLevel(permissionLevel);
        if (!plugin.config().general().overrideJoinMessage()) return;
        var id = event.getPlayer().hasPlayedBefore()
                ? plugin.getServer().getOnlinePlayers().size()
                : plugin.getServer().getOfflinePlayers().length;
        var message = event.getPlayer().hasPlayedBefore() ? "player.connected" : "player.welcome";
        var resolvers = plugin.serviceResolvers(event.getPlayer())
                .resolver(Placeholder.parsed("id", String.valueOf(id)))
                .build();
        plugin.getServer().forEachAudience(audience -> plugin.bundle().sendMessage(
                audience, message, resolvers
        ));
        event.joinMessage(null);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!plugin.config().general().overrideQuitMessage()) return;
        var resolvers = plugin.serviceResolvers(event.getPlayer()).build();
        plugin.getServer().forEachAudience(audience -> plugin.bundle().sendMessage(
                audience, "player.disconnected", resolvers
        ));
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
