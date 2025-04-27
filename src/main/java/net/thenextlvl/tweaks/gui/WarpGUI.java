package net.thenextlvl.tweaks.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.model.NamedLocation;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;

import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.PLUGIN;

@NullMarked
public class WarpGUI extends NamedLocationGUI {
    public WarpGUI(TweaksPlugin plugin, Player owner, Collection<NamedLocation> elements) {
        super(plugin, plugin.config().guis.warps, owner,  Component.translatable("gui.title.warps"), elements);
    }

    @Override
    protected void teleport(NamedLocation element) {
        plugin.teleportController().teleport(owner, element, PLUGIN).thenAccept(success -> {
            var message = success ? "command.warp" : "command.teleport.cancelled";
            plugin.bundle().sendMessage(owner, message, Placeholder.parsed("name", element.getName()));
        });
    }
}
