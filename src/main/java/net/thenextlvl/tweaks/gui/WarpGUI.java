package net.thenextlvl.tweaks.gui;

import core.paper.item.ItemBuilder;
import io.papermc.paper.datacomponent.DataComponentTypes;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.Collection;

import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.PLUGIN;

@NullMarked
public final class WarpGUI {
    private static final TweaksPlugin plugin = JavaPlugin.getPlugin(TweaksPlugin.class);
    private static final Material[] MATERIALS = Arrays.stream(Material.values())
            .filter(material -> !material.isLegacy() && material.isItem()).toArray(Material[]::new);

    private WarpGUI() {
    }

    public static PaginatedInterface<NamedLocation> create(final Collection<NamedLocation> elements) {
        final var base = Interface.builder()
                .title(player -> plugin.bundle().component("gui.title.warps", player))
                .layout(Layout.builder(
                                "         ",
                                " XXXXXXX ",
                                " XXXXXXX ",
                                " XXXXXXX ",
                                "  < x >  ")
                        .mask(' ', ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).hideTooltip().build())
                        .build())
                .slot('x', context -> {
                    return ItemBuilder.of(Material.BARRIER)
                            .itemName(plugin.bundle().component("gui.close", context.player()))
                            .build();
                }, context -> context.player().closeInventory())
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
                    // todo: do not show "right click to delete" for unprivileged players
                    return display(element, context.player());
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
                })).build(plugin);
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
                        .mask('i', context -> display(element, context.player())) // todo: only show icon
                        .mask(' ', ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).hideTooltip().build())
                        .build())
                .slot('y', context -> {
                    return ItemBuilder.of(Material.LIME_STAINED_GLASS_PANE) // todo: green instead of lime?
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

    private static ItemStack display(final NamedLocation element, final Player player) {
        return ItemBuilder.of(icon(element))
                .unsetData(DataComponentTypes.BUNDLE_CONTENTS)
                .itemName(plugin.bundle().component("gui.item.location", player,
                        Argument.string("name", element.getName())))
                .lore(plugin.bundle().components("gui.item.location.lore", player,
                        Placeholder.parsed("world", element.getWorld() != null ? element.getWorld().key().asString() : "-/-"),
                        Formatter.number("x", element.x()),
                        Formatter.number("y", element.y()),
                        Formatter.number("z", element.z()),
                        Formatter.number("yaw", element.getYaw()),
                        Formatter.number("pitch", element.getPitch())))
                .build();
    }

    private static Material icon(final NamedLocation element) {
        final var configured = plugin.config().guis.nameIcons.get(element.getName());
        return configured != null ? configured : MATERIALS[Math.floorMod(element.getName().hashCode(), MATERIALS.length)];
    }
}
