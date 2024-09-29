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
        var permissionLevel = plugin.config().generalConfig().defaultPermissionLevel();
        if (permissionLevel != -1) event.getPlayer().sendOpLevel(permissionLevel);
        if (!plugin.config().generalConfig().overrideJoinMessage()) return;
        plugin.getServer().getOnlinePlayers().forEach(player -> plugin.bundle().sendMessage(player, "player.joined",
                Placeholder.parsed("player", event.getPlayer().getName())));
        event.joinMessage(null);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event) {
        if (!plugin.config().generalConfig().overrideQuitMessage()) return;
        plugin.getServer().getOnlinePlayers().forEach(player -> plugin.bundle().sendMessage(player, "player.left",
                Placeholder.parsed("player", event.getPlayer().getName())));
        event.quitMessage(null);
    }
}
