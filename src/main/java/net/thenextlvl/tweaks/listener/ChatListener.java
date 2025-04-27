package net.thenextlvl.tweaks.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.translation.Argument;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.controller.ServiceController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

@NullMarked
public class ChatListener implements Listener {
    private final TweaksPlugin plugin;

    public ChatListener(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onChat(AsyncChatEvent event) {
        if (!plugin.config().general.logChat)
            event.viewers().remove(plugin.getServer().getConsoleSender());
        if (!plugin.config().general.overrideChat) return;
        var messageContent = event.signedMessage().message();
        event.renderer((source, displayName, message, viewer) -> {
            var arguments = Argument.tagResolver(plugin.serviceResolvers(source).resolvers(
                    Placeholder.component("display_name", displayName),
                    Placeholder.component("message", message),
                    Placeholder.parsed("message_content", messageContent),
                    createDeleteTag(source, viewer, event.signedMessage())
            ).build());
            return Objects.requireNonNull(plugin.bundle().translate("chat.format", viewer, arguments));
        });
    }

    private TagResolver.Single createDeleteTag(Player sender, Audience audience, SignedMessage message) {
        if (!(audience instanceof Player viewer)) return Placeholder.parsed("delete", "");
        if (!canDelete(viewer, sender)) return Placeholder.parsed("delete", "");
        var component = plugin.bundle().translate("chat.format.delete", viewer);
        if (component == null) return Placeholder.parsed("delete", "");
        return Placeholder.component("delete", component.clickEvent(ClickEvent.callback(ignored -> {
            if (!canDelete(viewer, sender)) return;
            plugin.getServer().getOnlinePlayers().forEach(all -> all.deleteMessage(message));
        }, ClickCallback.Options.builder().uses(1).lifetime(
                Duration.ofMillis(plugin.config().general.messageDeletionTimeout)
        ).build())));
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean canDelete(Player viewer, Player source) {
        if (source.equals(viewer) && viewer.hasPermission("tweaks.chat.delete.own")) return true;
        if (services().isPresent() && getWeight(source) <= getChatDeleteWeight(viewer)) return true;
        return viewer.hasPermission("tweaks.chat.delete");
    }

    private int getChatDeleteWeight(Player player) {
        return services().map(services -> services.getChatDeleteWeight(player)).orElse(-1);
    }

    private int getWeight(Player player) {
        return services().map(services -> services.getWeight(player)).orElse(-1);
    }

    private Optional<ServiceController> services() {
        return Optional.ofNullable(plugin.serviceController());
    }
}
