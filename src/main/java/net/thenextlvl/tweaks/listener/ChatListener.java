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
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.luckperms.api.LuckPermsProvider;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.*;

@RequiredArgsConstructor
public class ChatListener implements Listener {
    private final ClickCallback.Options options = ClickCallback.Options.builder().uses(1).build();
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
                        TagResolver.resolver("signature", Tag.preProcessParsed(toString(event.signedMessage()))),
                        TagResolver.resolver("delete", (args, context) -> createDeleteTag(args, source, viewer))
                ).build()));
    }

    private TagResolver.Builder luckResolvers(Player player) {
        if (!isLuckPermsLoaded()) return TagResolver.builder();
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

    private Tag createDeleteTag(ArgumentQueue args, Player source, Audience viewer) {
        var empty = Tag.selfClosingInserting(Component.empty());
        if (!(viewer instanceof Player player)) return empty;
        else if (source.equals(viewer) && !player.hasPermission("tweaks.chat.delete.own")) return empty;
        else if (isLuckPermsLoaded() && getWeight(source) > getChatDeleteWeight(player)) return empty;
        else if (!player.hasPermission("tweaks.chat.delete")) return empty;
        var signature = stringToSignature(args.popOr("The <delete> tag requires a message signature").value());
        return Tag.selfClosingInserting(MiniMessage.miniMessage()
                .deserialize(plugin.bundle().format(player.locale(), "chat.format.delete"))
                .clickEvent(ClickEvent.callback(audience -> Bukkit.getOnlinePlayers().forEach(all ->
                        all.deleteMessage(signature)), options)));
    }

    private boolean isLuckPermsLoaded() {
        return Bukkit.getPluginManager().isPluginEnabled("LuckPerms");
    }

    private int getWeight(Player player) {
        if (!isLuckPermsLoaded()) return 0;
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
                    return split.length == 4
                            && split[0].equals("tweaks")
                            && split[1].equals("chat")
                            && split[2].equals("delete")
                            && !split[3].equals("all")
                            && !split[3].equals("own");
                })
                .map(permission -> {
                    try {
                        return Integer.parseInt(permission.split("\\.")[3]);
                    } catch (NumberFormatException ignored) {
                        return 0;
                    }
                })
                .filter(weight -> weight >= 0)
                .max(Integer::compareTo)
                .orElse(0));
    }

    private String toString(SignedMessage signedMessage) {
        var signature = signedMessage.signature();
        if (signature == null) return "";
        var strings = new ArrayList<String>();
        for (var b : signature.bytes()) strings.add(String.valueOf(b));
        return String.join(",", strings);
    }

    private SignedMessage.Signature stringToSignature(String string) {
        if (string.isBlank()) return () -> new byte[0];
        var strings = Arrays.asList(string.split(","));
        var bytes = new byte[strings.size()];
        for (var i = 0; i < strings.size(); i++) bytes[i] = Byte.parseByte(strings.get(i));
        return () -> bytes;
    }
}
