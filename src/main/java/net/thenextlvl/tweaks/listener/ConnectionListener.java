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
        if (!plugin.config().generalConfig().overrideJoinMessage()) return;
        Bukkit.getOnlinePlayers().forEach(player -> player.sendRichMessage(Messages.JOIN_MESSAGE.message(
                event.getPlayer().locale(), event.getPlayer(), Placeholder.of("player", Player::getName))
        ));
        event.joinMessage(null);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event) {
        if (!plugin.config().generalConfig().overrideQuitMessage()) return;
        Bukkit.getOnlinePlayers().forEach(player -> player.sendRichMessage(Messages.QUIT_MESSAGE.message(
                event.getPlayer().locale(), event.getPlayer(), Placeholder.of("player", Player::getName))
        ));
        event.quitMessage(null);
    }
}
