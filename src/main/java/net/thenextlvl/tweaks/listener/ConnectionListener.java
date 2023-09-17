package net.thenextlvl.tweaks.listener;

import core.api.placeholder.Placeholder;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
        Bukkit.getOnlinePlayers().forEach(player -> {
            var message = Messages.JOIN_MESSAGE.message(event.getPlayer().locale(), event.getPlayer(),
                    Placeholder.of("player", Player::getName));
            if (!message.isEmpty()) player.sendRichMessage(message);
        });
        event.joinMessage(null);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event) {
        if (!plugin.config().generalConfig().overrideQuitMessage()) return;
        Bukkit.getOnlinePlayers().forEach(player -> {
            var message = Messages.QUIT_MESSAGE.message(event.getPlayer().locale(), event.getPlayer(),
                    Placeholder.of("player", Player::getName));
            if (!message.isEmpty()) player.sendRichMessage(message);
        });
        event.quitMessage(null);
    }
}
