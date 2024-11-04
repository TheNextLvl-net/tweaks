package net.thenextlvl.tweaks.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.controller.ServiceController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;
import java.util.Optional;

@NullMarked
@RequiredArgsConstructor
public class ChatListener implements Listener {
    private final TweaksPlugin plugin;

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onChat(AsyncChatEvent event) {
        if (!plugin.config().general().logChat())
            event.viewers().remove(plugin.getServer().getConsoleSender());
        if (!plugin.config().general().overrideChat()) return;
        var messageContent = event.message() instanceof TextComponent text ? text.content() : "";
        event.renderer((source, displayName, message, viewer) -> plugin.bundle().component(viewer, "chat.format",
                serviceResolvers(source).resolvers(
                        Placeholder.component("custom_name", Optional.ofNullable(source.customName())
                                .orElseGet(source::name)),
                        Placeholder.component("display_name", displayName),
                        Placeholder.component("message", message),
                        Placeholder.parsed("message_content", messageContent),
                        Placeholder.parsed("player", source.getName()),
                        Placeholder.parsed("world", source.getWorld().getName()),
                        Placeholder.parsed("language_tag", String.valueOf(source.locale().toLanguageTag())),
                        Placeholder.parsed("locale", String.valueOf(source.locale().getDisplayName(source.locale()))),
                        createDeleteTag(source, viewer, event.signedMessage())
                ).build()));
    }

    private TagResolver.Builder serviceResolvers(Player player) {
        var resolver = TagResolver.builder().resolvers(
                Placeholder.parsed("balance", ""),
                Placeholder.parsed("balance", ""),
                Placeholder.parsed("balance_unformatted", ""),
                Placeholder.parsed("balance_unformatted", ""),
                Placeholder.parsed("bank_balance", ""),
                Placeholder.parsed("bank_balance", ""),
                Placeholder.parsed("bank_balance_unformatted", ""),
                Placeholder.parsed("bank_balance_unformatted", ""),
                Placeholder.parsed("chat_display_name", player.getName()),
                Placeholder.parsed("chat_prefix", ""),
                Placeholder.parsed("chat_suffix", ""),
                Placeholder.parsed("currency_name", ""),
                Placeholder.parsed("currency_name_plural", ""),
                Placeholder.parsed("currency_symbol", ""),
                Placeholder.parsed("group", ""),
                Placeholder.parsed("group_prefix", ""),
                Placeholder.parsed("group_suffix", "")
        );
        return services().map(services -> services.serviceResolvers(player))
                .map(resolver::resolvers)
                .orElse(resolver);
    }

    private TagResolver.Single createDeleteTag(Player sender, Audience audience, SignedMessage message) {
        if (!(audience instanceof Player viewer)) return Placeholder.parsed("delete", "");
        if (!canDelete(viewer, sender)) return Placeholder.parsed("delete", "");
        var component = plugin.bundle().nullable(viewer.locale(), "chat.format.delete");
        if (component == null) return Placeholder.parsed("delete", "");
        return Placeholder.component("delete", component.clickEvent(ClickEvent.callback(ignored -> {
            if (!canDelete(viewer, sender)) return;
            plugin.getServer().getOnlinePlayers().forEach(all -> all.deleteMessage(message));
        }, ClickCallback.Options.builder().uses(1).lifetime(
                Duration.ofMillis(plugin.config().general().messageDeletionTimeout())
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
