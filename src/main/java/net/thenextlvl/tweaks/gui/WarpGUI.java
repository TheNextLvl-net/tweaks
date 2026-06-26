package net.thenextlvl.tweaks.gui;

import core.paper.item.ItemBuilder;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.translation.Argument;
import net.thenextlvl.interfaces.ActionItem;
import net.thenextlvl.interfaces.Interface;
import net.thenextlvl.interfaces.Layout;
import net.thenextlvl.interfaces.PaginatedInterface;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.model.NamedLocation;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.Collection;

import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.PLUGIN;

@NullMarked
public final class WarpGUI {
    private static final String NEXT = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQ4NjVhYWUyNzQ2YTliOGU5YTRmZTYyOWZiMDhkMThkMGE5MjUxZTVjY2JlNWZhNzA1MWY1M2VhYjliOTQifX19";
    private static final String PLUS = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjliODYxYWFiYjMxNmM0ZWQ3M2I0ZTU0MjgzMDU3ODJlNzM1NTY1YmEyYTA1MzkxMmUxZWZkODM0ZmE1YTZmIn19fQ==";
    private static final String PREVIOUS = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTJmMDQyNWQ2NGZkYzg5OTI5MjhkNjA4MTA5ODEwYzEyNTFmZTI0M2Q2MGQxNzViZWQ0MjdjNjUxY2JlIn19fQ==";

    private static final TweaksPlugin plugin = JavaPlugin.getPlugin(TweaksPlugin.class);
    private static final Material[] MATERIALS = Arrays.stream(Material.values())
            .filter(material -> !material.isLegacy() && material.isItem())
            .toArray(Material[]::new);

    private WarpGUI() {
    }

    public static PaginatedInterface<NamedLocation> create(final Collection<NamedLocation> elements) {
        final var base = Interface.builder()
                .title(player -> plugin.bundle().component("gui.title.warps", player,
                        Formatter.number("warps", elements.size())))
                .layout(Layout.builder(
                                "         ",
                                " XXXXXXX ",
                                " XXXXXXX ",
                                " XXXXXXX ",
                                "  < + >  ")
                        .mask(' ', ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).hideTooltip().build())
                        .build())
                .slot('<', context -> {
                    return ItemBuilder.of(Material.PLAYER_HEAD)
                            .profileValue(PREVIOUS)
                            .itemName(plugin.bundle().component("gui.page.previous", context.player()))
                            .build();
                }, context -> context.paginatedSession().ifPresent(session -> {
                    session.setPage(session.getCurrentPage() - 1);
                }))
                .slot('+', context -> {
                    return ItemBuilder.of(Material.PLAYER_HEAD)
                            .profileValue(PLUS)
                            .itemName(plugin.bundle().component("gui.item.warp.set", context.player()))
                            .build();
                }, context -> context.paginatedSession().ifPresent(session -> {
                }))
                .slot('>', context -> {
                    return ItemBuilder.of(Material.PLAYER_HEAD)
                            .profileValue(NEXT)
                            .itemName(plugin.bundle().component("gui.page.next", context.player()))
                            .build();
                }, context -> context.paginatedSession().ifPresent(session -> {
                    session.setPage(session.getCurrentPage() + 1);
                }));
        return PaginatedInterface.<NamedLocation>builder(base)
                .transformer(element -> new ActionItem(context -> {
                    final var player = context.player();
                    final var builder = buildDisplay(element, player);
                    builder.appendLore(Component.empty());
                    builder.appendLore(plugin.bundle().components("gui.item.location.lore.teleport", player));
                    if (player.hasPermission("tweaks.command.warp.delete")) {
                        builder.appendLore(plugin.bundle().components("gui.item.location.lore.delete", player));
                    }
                    return builder.build();
                }, context -> {
                    final var player = context.player();
                    if (context.clickType().isLeftClick()) {
                        plugin.teleportController().teleport(player, element, PLUGIN).thenAccept(success -> {
                            final var message = success ? "command.warp" : "command.teleport.cancelled";
                            plugin.bundle().sendMessage(player, message, Placeholder.parsed("name", element.getName()));
                        });
                        player.closeInventory();
                    } else if (context.clickType().isRightClick()) {
                        if (context.player().hasPermission("tweaks.command.warp.delete")) {
                            delete(element).open(context.player());
                        }
                    }
                }))
                .mask('X')
                .content(elements)
                .build(plugin);
    }

    private static Interface delete(final NamedLocation element) {
        return Interface.builder()
                .title(player -> plugin.bundle().component("gui.title.warps.delete", player,
                        Placeholder.parsed("warp", element.getName())))
                .layout(Layout.builder(
                                "    i    ",
                                " nnn yyy ",
                                " nnn yyy ",
                                " nnn yyy ",
                                "         ")
                        .mask('i', context -> buildDisplay(element, context.player()).build())
                        .mask(' ', ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).hideTooltip().build())
                        .build())
                .slot('y', context -> {
                    return ItemBuilder.of(Material.LIME_STAINED_GLASS_PANE)
                            .itemName(plugin.bundle().component("gui.item.warp.delete", context.player(),
                                    Placeholder.parsed("warp", element.getName())))
                            .build();
                }, context -> {
                    plugin.warpController().deleteWarp(element.getName()).thenCompose(success -> {
                        final var message = success ? "command.warp.delete" : "command.warp.unknown";
                        plugin.bundle().sendMessage(context.player(), message,
                                Placeholder.parsed("name", element.getName()));
                        return plugin.warpController().getWarps();
                    }).thenAccept(warps -> {
                        final var interface_ = WarpGUI.create(warps);
                        context.player().getScheduler().run(plugin, task -> interface_.open(context.player()), null);
                    });
                })
                .slot('n', context -> {
                    return ItemBuilder.of(Material.RED_STAINED_GLASS_PANE)
                            .itemName(plugin.bundle().component("gui.item.warp.delete.cancel", context.player(),
                                    Placeholder.parsed("warp", element.getName())))
                            .build();
                }, context -> {
                    plugin.warpController().getWarps().thenAccept(warps -> {
                        final var interface_ = WarpGUI.create(warps);
                        context.player().getScheduler().run(plugin, task -> interface_.open(context.player()), null);
                    });
                })
                .build();
    }

    private static ItemBuilder buildDisplay(final NamedLocation element, final Player player) {
        return ItemBuilder.of(icon(element))
                .unsetData(DataComponentTypes.BUNDLE_CONTENTS)
                .unsetData(DataComponentTypes.ATTRIBUTE_MODIFIERS)
                .itemName(plugin.bundle().component("gui.item.location", player,
                        Argument.string("name", element.getName())))
                .lore(plugin.bundle().components("gui.item.location.lore", player,
                        Placeholder.parsed("world", element.getWorld() != null ? element.getWorld().key().asString() : "-/-"),
                        Formatter.number("x", element.x()),
                        Formatter.number("y", element.y()),
                        Formatter.number("z", element.z()),
                        Formatter.number("yaw", element.getYaw()),
                        Formatter.number("pitch", element.getPitch())));
    }

    private static Material icon(final NamedLocation element) {
        final var configured = plugin.config().guis.nameIcons.get(element.getName());
        return configured != null ? configured : MATERIALS[Math.floorMod(element.getName().hashCode(), MATERIALS.length)];
    }
}
