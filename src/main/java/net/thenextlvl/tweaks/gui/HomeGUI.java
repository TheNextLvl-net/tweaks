package net.thenextlvl.tweaks.gui;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.model.NamedLocation;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;

import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.PLUGIN;

@NullMarked
public class HomeGUI extends NamedLocationGUI {
    public HomeGUI(TweaksPlugin plugin, Player owner, Collection<NamedLocation> elements) {
        super(plugin, plugin.config().guis().homes(), owner, plugin.bundle().component(owner, "gui.title.homes"), elements);
    }

    @Override
    protected void teleport(NamedLocation element) {
        plugin.teleportController().teleport(owner, element, PLUGIN).thenAccept(success -> {
            var message = success ? "command.home.name" : "command.teleport.cancelled";
            plugin.bundle().sendMessage(owner, message, Placeholder.parsed("name", element.getName()));
        });
    }
}
