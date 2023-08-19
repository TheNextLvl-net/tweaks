package net.thenextlvl.tweaks.listener;

import core.api.placeholder.Placeholder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        event.joinMessage(MiniMessage.miniMessage().deserialize(Messages.JOIN_MESSAGE.message(
                event.getPlayer().locale(), event.getPlayer(), Placeholder.of("player", Player::getName))
        ));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event) {
        event.quitMessage(MiniMessage.miniMessage().deserialize(Messages.QUIT_MESSAGE.message(
                event.getPlayer().locale(), event.getPlayer(), Placeholder.of("player", Player::getName))
        ));
    }
}
