package net.thenextlvl.tweaks.listener;

import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ConnectionListener implements Listener {
    private final TweaksPlugin plugin;

    public ConnectionListener(final TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(final PlayerJoinEvent event) {
        final var permissionLevel = plugin.config().general.defaultPermissionLevel;
        if (permissionLevel != -1) event.getPlayer().sendOpLevel(permissionLevel);
        if (!plugin.config().general.overrideJoinMessage) return;
        final var id = event.getPlayer().hasPlayedBefore()
                ? plugin.getServer().getOnlinePlayers().size()
                : plugin.getServer().getOfflinePlayers().length;
        final var message = event.getPlayer().hasPlayedBefore() ? "player.connected" : "player.welcome";
        final var resolvers = plugin.serviceResolvers(event.getPlayer())
                .resolver(Formatter.number("id", id))
                .build();
        plugin.getServer().forEachAudience(audience -> plugin.bundle().sendMessage(
                audience, message, resolvers
        ));
        event.joinMessage(null);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        if (!plugin.config().general.overrideQuitMessage) return;
        final var resolvers = plugin.serviceResolvers(event.getPlayer()).build();
        plugin.getServer().forEachAudience(audience -> plugin.bundle().sendMessage(
                audience, "player.disconnected", resolvers
        ));
        event.quitMessage(null);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void cleanup(final PlayerQuitEvent event) {
        plugin.msgController().removeConversations(event.getPlayer());
        plugin.tpaController().removeRequests(event.getPlayer());
        plugin.teleportController().remove(event.getPlayer());
        plugin.backController().remove(event.getPlayer());
    }
}
