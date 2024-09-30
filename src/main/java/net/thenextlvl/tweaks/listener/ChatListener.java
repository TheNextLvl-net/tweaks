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
import net.thenextlvl.service.api.group.GroupHolder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.controller.ServiceController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

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
        var resolver = TagResolver.builder();
        var services = services();
        services.map(ServiceController::getGroups).flatMap(groups -> groups.getGroupHolder(player)
                .map(GroupHolder::getPrimaryGroup).flatMap(groups::getGroup)).ifPresent(group -> {
            group.getDisplayName().ifPresent(displayName -> resolver.resolver(Placeholder.parsed("group", displayName)));
            group.getPrefix().ifPresent(prefix -> resolver.resolver(Placeholder.parsed("group_prefix", prefix)));
            group.getSuffix().ifPresent(suffix -> resolver.resolver(Placeholder.parsed("group_suffix", suffix)));
            resolver.resolver(Placeholder.parsed("group_name", group.getName()));
        });
        services.map(ServiceController::getChat).flatMap(chat -> chat.getProfile(player)).ifPresent(profile -> {
            profile.getDisplayName().ifPresent(displayName -> resolver.resolver(Placeholder.parsed("chat_display_name", displayName)));
            profile.getPrefix().ifPresent(prefix -> resolver.resolver(Placeholder.parsed("chat_prefix", prefix)));
            profile.getSuffix().ifPresent(suffix -> resolver.resolver(Placeholder.parsed("chat_suffix", suffix)));
        });
        services.map(ServiceController::getEconomy).ifPresent(economy -> {
            economy.getAccount(player).ifPresent(account -> {
                resolver.resolver(Placeholder.parsed("balance", economy.format(account.getBalance())));
                resolver.resolver(Placeholder.parsed("balance_unformatted", account.getBalance().toString()));
            });
            economy.getBankController().flatMap(controller -> controller.getBank(player)).ifPresent(bank -> {
                resolver.resolver(Placeholder.parsed("bank_balance", bank.getBalance().toString()));
                resolver.resolver(Placeholder.parsed("bank_balance_unformatted", bank.getBalance().toString()));
            });
            resolver.resolver(Placeholder.parsed("currency_name", economy.getCurrencyNameSingular(player.locale())));
            resolver.resolver(Placeholder.parsed("currency_name_plural", economy.getCurrencyNamePlural(player.locale())));
            resolver.resolver(Placeholder.parsed("currency_symbol", economy.getCurrencySymbol()));
        });
        return resolver;
    }

    private TagResolver.Single createDeleteTag(Player sender, Audience audience, SignedMessage message) {
        if (!(audience instanceof Player viewer)) return Placeholder.parsed("delete", "");
        if (!canDelete(viewer, sender)) return Placeholder.parsed("delete", "");
        var component = plugin.bundle().nullable(viewer.locale(), "chat.format.delete");
        if (component == null) return Placeholder.parsed("delete", "");
        return Placeholder.component("delete", component.clickEvent(ClickEvent.callback(ignored -> {
            if (!canDelete(viewer, sender)) return;
            plugin.getServer().getOnlinePlayers().forEach(all -> all.deleteMessage(message));
        }, ClickCallback.Options.builder().uses(1).lifetime(Duration.ofMinutes(10)).build())));
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean canDelete(Player viewer, Player source) {
        if (source.equals(viewer) && viewer.hasPermission("tweaks.chat.delete.own")) return true;
        if (services().isPresent() && getWeight(source) <= getChatDeleteWeight(viewer)) return true;
        return viewer.hasPermission("tweaks.chat.delete");
    }

    private int getWeight(Player player) {
        return services().map(ServiceController::getGroups)
                .flatMap(controller -> controller.getGroupHolder(player)
                        .map(GroupHolder::getPrimaryGroup)
                        .flatMap(controller::getGroup))
                .map(group -> group.getWeight().orElse(0))
                .orElse(0);
    }

    private final Map<Player, Integer> chatDeleteWeights = new WeakHashMap<>();

    private int getChatDeleteWeight(Player player) {
        return chatDeleteWeights.computeIfAbsent(player, p -> p.getEffectivePermissions().stream()
                .map(PermissionAttachmentInfo::getPermission)
                .filter(permission -> permission.startsWith("tweaks.chat.delete."))
                .filter(permission -> {
                    var split = permission.split("\\.");
                    return split.length == 4 && !split[3].equals("own");
                })
                .map(permission -> {
                    try {
                        return Integer.parseInt(permission.split("\\.")[3]);
                    } catch (NumberFormatException ignored) {
                        return -1;
                    }
                })
                .filter(weight -> weight >= 0)
                .max(Integer::compareTo)
                .orElse(-1));
    }

    private Optional<ServiceController> services() {
        return Optional.ofNullable(plugin.serviceController());
    }
}
