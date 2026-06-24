package net.thenextlvl.tweaks.gui;

import core.paper.item.ItemBuilder;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.translation.Argument;
import net.thenextlvl.interfaces.ActionItem;
import net.thenextlvl.interfaces.Interface;
import net.thenextlvl.interfaces.Layout;
import net.thenextlvl.interfaces.PaginatedInterface;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.model.NamedLocation;
import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.Collection;

import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.PLUGIN;

@NullMarked
public final class HomeGUI {
    private static final Material[] MATERIALS = Arrays.stream(Material.values())
            .filter(material -> !material.isLegacy() && material.isItem()).toArray(Material[]::new);

    private HomeGUI() {
    }

    public static PaginatedInterface<NamedLocation> create(
            final TweaksPlugin plugin, final Collection<NamedLocation> elements
    ) {
        final var base = Interface.builder()
                .title(player -> plugin.bundle().component("gui.title.homes", player))
                .layout(Layout.builder(
                                "         ",
                                " XXXXXXX ",
                                " XXXXXXX ",
                                " XXXXXXX ",
                                "   < >   ")
                        .mask(' ', ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).hideTooltip().build())
                        .build())
                .slot('<', context -> {
                    return ItemBuilder.of(Material.ARROW)
                            .itemName(plugin.bundle().component("gui.page.previous", context.player()))
                            .build();
                }, context -> context.paginatedSession().ifPresent(session -> {
                    session.setPage(session.getCurrentPage() - 1);
                }))
                .slot('>', context -> {
                    return ItemBuilder.of(Material.ARROW)
                            .itemName(plugin.bundle().component("gui.page.next", context.player()))
                            .build();
                }, context -> context.paginatedSession().ifPresent(session -> {
                    session.setPage(session.getCurrentPage() + 1);
                }));
        return PaginatedInterface.<NamedLocation>builder(base).mask('X').content(elements)
                .transformer(element -> new ActionItem(context -> {
                    return ItemBuilder.of(icon(plugin, element))
                            .itemName(plugin.bundle().component("gui.item.location", context.player(),
                                    Argument.string("name", element.getName())))
                            .build();
                }, context -> {
                    final var player = context.player();
                    plugin.teleportController().teleport(player, element, PLUGIN).thenAccept(success -> {
                        final var message = success ? "command.home.name" : "command.teleport.cancelled";
                        plugin.bundle().sendMessage(player, message, Placeholder.parsed("name", element.getName()));
                    });
                    player.closeInventory();
                })).build(plugin);
    }

    private static Material icon(final TweaksPlugin plugin, final NamedLocation element) {
        final var configured = plugin.config().guis.nameIcons.get(element.getName());
        return configured != null ? configured : MATERIALS[Math.floorMod(element.getName().hashCode(), MATERIALS.length)];
    }
}
