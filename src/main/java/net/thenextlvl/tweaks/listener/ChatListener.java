package net.thenextlvl.tweaks.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.query.QueryOptions;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class ChatListener implements Listener {
    private final TweaksPlugin plugin;

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onChat(AsyncChatEvent event) {
        if (!plugin.config().generalConfig().logChat())
            event.viewers().remove(Bukkit.getConsoleSender());
        if (!plugin.config().generalConfig().overrideChat()) return;
        var messageContent = event.message() instanceof TextComponent text ? text.content() : "";
        event.renderer((source, displayName, message, viewer) -> MiniMessage.miniMessage().deserialize(
                plugin.config().formattingConfig().chatFormat(),
                luckResolvers(source).resolvers(
                        TagResolver.resolver("display_name", Tag.inserting(displayName)),
                        TagResolver.resolver("message_content", Tag.preProcessParsed(messageContent)),
                        TagResolver.resolver("message", Tag.inserting(message)),
                        TagResolver.resolver("player", Tag.inserting(source.name())),
                        TagResolver.resolver("world", Tag.preProcessParsed(source.getWorld().getName()))
                ).build()));
    }

    private TagResolver.Builder luckResolvers(Player player) {
        if (!Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) return TagResolver.builder();
        var user = LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(player);
        var group = LuckPermsProvider.get().getGroupManager().getGroup(user.getPrimaryGroup());
        var meta = user.getCachedData().getMetaData(QueryOptions.defaultContextualOptions());
        var groupName = group != null ? group.getDisplayName() != null ? group.getDisplayName() : group.getName() : "";
        var prefix = meta.getPrefix() != null ? meta.getPrefix() : "";
        var suffix = meta.getSuffix() != null ? meta.getSuffix() : "";
        return TagResolver.builder().resolvers(
                TagResolver.resolver("group", Tag.inserting(Component.text(groupName))),
                TagResolver.resolver("prefix", Tag.inserting(Component.text(prefix))),
                TagResolver.resolver("suffix", Tag.inserting(Component.text(suffix)))
        );
    }
}
