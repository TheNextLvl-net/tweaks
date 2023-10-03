package net.thenextlvl.tweaks.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.luckperms.api.LuckPermsProvider;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.Map;
import java.util.WeakHashMap;

@RequiredArgsConstructor
public class ChatListener implements Listener {
    private final ClickCallback.Options options = ClickCallback.Options.builder().uses(1).build();
    private final boolean luckperms = Bukkit.getPluginManager().isPluginEnabled("LuckPerms");
    private final TweaksPlugin plugin;

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onChat(AsyncChatEvent event) {
        if (!plugin.config().generalConfig().logChat())
            event.viewers().remove(Bukkit.getConsoleSender());
        if (!plugin.config().generalConfig().overrideChat()) return;
        var messageContent = event.message() instanceof TextComponent text ? text.content() : "";
        event.renderer((source, displayName, message, viewer) -> plugin.bundle().component(viewer, "chat.format",
                luckResolvers(source).resolvers(
                        TagResolver.resolver("display_name", Tag.inserting(displayName)),
                        TagResolver.resolver("message_content", Tag.preProcessParsed(messageContent)),
                        TagResolver.resolver("message", Tag.inserting(message)),
                        TagResolver.resolver("player", Tag.preProcessParsed(source.getName())),
                        TagResolver.resolver("world", Tag.preProcessParsed(source.getWorld().getName())),
                        TagResolver.resolver("delete", createDeleteTag(source, viewer, event.signedMessage()))
                ).build()));
    }

    private TagResolver.Builder luckResolvers(Player player) {
        if (!luckperms) return TagResolver.builder();
        var user = LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(player);
        var group = LuckPermsProvider.get().getGroupManager().getGroup(user.getPrimaryGroup());
        var meta = user.getCachedData().getMetaData(user.getQueryOptions());
        var groupName = group != null ? group.getDisplayName() != null ? group.getDisplayName() : group.getName() : "";
        var prefix = meta.getPrefix() != null ? meta.getPrefix() : "";
        var suffix = meta.getSuffix() != null ? meta.getSuffix() : "";
        return TagResolver.builder().resolvers(
                TagResolver.resolver("group", Tag.preProcessParsed(groupName)),
                TagResolver.resolver("prefix", Tag.preProcessParsed(prefix)),
                TagResolver.resolver("suffix", Tag.preProcessParsed(suffix))
        );
    }

    private Tag createDeleteTag(Player sender, Audience audience, SignedMessage signedMessage) {
        var empty = Tag.selfClosingInserting(Component.empty());
        if (!(audience instanceof Player viewer)) return empty;
        if (!canDelete(viewer, sender)) return empty;
        return Tag.selfClosingInserting(MiniMessage.miniMessage()
                .deserialize(plugin.bundle().format(viewer.locale(), "chat.format.delete"))
                .clickEvent(ClickEvent.callback(ignored -> {
                    if (canDelete(viewer, sender)) Bukkit.getOnlinePlayers()
                            .forEach(all -> all.deleteMessage(signedMessage));
                }, options)));
    }

    private boolean canDelete(Player viewer, Player source) {
        if (source.equals(viewer) && viewer.hasPermission("tweaks.chat.delete.own")) return true;
        if (luckperms && getWeight(source) <= getChatDeleteWeight(viewer)) return true;
        return viewer.hasPermission("tweaks.chat.delete");
    }

    private int getWeight(Player player) {
        if (!luckperms) return 0;
        var user = LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(player);
        var group = LuckPermsProvider.get().getGroupManager().getGroup(user.getPrimaryGroup());
        return group != null ? group.getWeight().orElse(0) : 0;
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
}
