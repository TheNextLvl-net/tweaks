package net.thenextlvl.tweaks.gui;

import core.paper.item.ItemBuilder;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.TextDialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.translation.Argument;
import net.thenextlvl.interfaces.ActionItem;
import net.thenextlvl.interfaces.Interface;
import net.thenextlvl.interfaces.Layout;
import net.thenextlvl.interfaces.PaginatedInterface;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.model.NamedLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

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
                }, context -> context.player().showDialog(setWarp(context.player())))
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
                .unsetData(DataComponentTypes.ATTRIBUTE_MODIFIERS)
                .unsetData(DataComponentTypes.BUNDLE_CONTENTS)
                .unsetData(DataComponentTypes.DEATH_PROTECTION)
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

    private static Dialog setWarp(final Player player) {
        final var location = player.getLocation();
        return setWarp(player, new WarpForm(
                "",
                location.getWorld().key().asString(),
                Double.toString(location.getX()),
                Double.toString(location.getY()),
                Double.toString(location.getZ()),
                Float.toString(location.getPitch()),
                Float.toString(location.getYaw())
        ), List.of());
    }

    private static Dialog setWarp(final Player player, final WarpForm form, final List<Component> errors) {
        final var save = ActionButton.builder(Component.text("Save", NamedTextColor.GREEN))
                .action(DialogAction.customClick((response, audience) -> saveWarp(player, response),
                        ClickCallback.Options.builder().uses(1).build()))
                .build();
        final var cancel = ActionButton.builder(Component.text("Cancel", NamedTextColor.RED))
                .action(DialogAction.customClick((response, audience) -> openWarps(player),
                        ClickCallback.Options.builder().uses(1).build()))
                .build();

        return Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(Component.text("Set Warp"))
                        .body(errors.stream().map(DialogBody::plainMessage).toList())
                        .inputs(Arrays.asList(
                                input("name", "Name", form.name()),
                                input("world", "World", form.world()),
                                input("x", "X", form.x()),
                                input("y", "Y", form.y()),
                                input("z", "Z", form.z()),
                                input("pitch", "Pitch", form.pitch()),
                                input("yaw", "Yaw", form.yaw())))
                        .build())
                .type(DialogType.confirmation(save, cancel)));
    }

    private static TextDialogInput input(final String key, final String label, final String initial) {
        return DialogInput.text(key, Component.text(label))
                .width(256)
                .initial(initial)
                .build();
    }

    private static void saveWarp(final Player player, final DialogResponseView response) {
        final var form = WarpForm.from(response);
        final var name = form.name().trim();
        final var errors = new ArrayList<String>();
        if (name.isEmpty()) {
            errors.add("Warp name cannot be empty"); // todo: add translations
        }

        final var worldName = form.world().trim();
        final var key = NamespacedKey.fromString(worldName);
        final var world = key != null ? Bukkit.getWorld(key) : Bukkit.getWorld(worldName);
        if (world == null) {
            errors.add("Unknown world: " + worldName);
        }

        final var x = parseDouble("X", form.x(), -30000000, 30000000);
        final var y = world != null
                ? parseDouble("Y", form.y(), world.getMinHeight(), world.getMaxHeight())
                : parseDouble("Y", form.y(), -30000000, 30000000);
        final var z = parseDouble("Z", form.z(), -30000000, 30000000);
        final var pitch = parseDouble("Pitch", form.pitch(), -90, 90);
        final var yaw = parseDouble("Yaw", form.yaw(), -180, 180);
        errors.addAll(errors(x, y, z, pitch, yaw));
        if (!errors.isEmpty()) {
            player.showDialog(setWarp(player, form, errors.stream()
                    .<Component>map(error -> Component.text(error, NamedTextColor.RED))
                    .toList()));
            return;
        }

        assert x.value() != null && y.value() != null && z.value() != null && pitch.value() != null && yaw.value() != null;
        final var location = new Location(world, x.value(), y.value(), z.value(), yaw.value().floatValue(), pitch.value().floatValue());
        plugin.warpController().getWarp(name).thenAccept(existing -> {
            existing.ifPresentOrElse(ignored -> player.getScheduler().run(plugin, task -> {
                player.showDialog(confirmOverride(player, form, name, location));
            }, null), () -> saveWarp(player, name, location));
        });
    }

    private static Dialog confirmOverride(final Player player, final WarpForm form, final String name, final Location location) {
        final var confirm = ActionButton.builder(Component.text("Override", NamedTextColor.GREEN))
                .action(DialogAction.customClick((response, audience) -> saveWarp(player, name, location),
                        ClickCallback.Options.builder().uses(1).build()))
                .build();
        final var cancel = ActionButton.builder(Component.text("Cancel", NamedTextColor.RED))
                .action(DialogAction.customClick((response, audience) -> player.showDialog(setWarp(player, form, List.of())),
                        ClickCallback.Options.builder().uses(1).build()))
                .build();

        return Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(Component.text("Override Warp"))
                        .body(List.of(DialogBody.plainMessage(Component.text("Warp \"" + name + "\" already exists. Override it?",
                                NamedTextColor.RED))))
                        .build())
                .type(DialogType.confirmation(confirm, cancel)));
    }

    private static void saveWarp(final Player player, final String name, final Location location) {
        plugin.warpController().setWarp(name, location).thenAccept(success -> {
            final var message = success ? "command.warp.set" : "nothing.changed";
            plugin.bundle().sendMessage(player, message, Placeholder.parsed("name", name));
            openWarps(player);
        });
    }

    private static void openWarps(final Player player) {
        plugin.warpController().getWarps().thenAccept(warps -> {
            final var interface_ = WarpGUI.create(warps);
            player.getScheduler().run(plugin, task -> interface_.open(player), null);
        });
    }

    private static String text(final DialogResponseView response, final String key) {
        final var value = response.getText(key);
        return value != null ? value : "";
    }

    private static ParseResult<Double> parseDouble(
            final String label,
            @Nullable final String input,
            final double min,
            final double max
    ) {
        if (input == null || input.isBlank()) return new ParseResult<>(null, label + " cannot be empty");
        try {
            final var value = Double.parseDouble(input.trim());
            if (value < min || value > max) return new ParseResult<>(null,
                    label + " must be between " + formatNumber(min) + " and " + formatNumber(max));
            return new ParseResult<>(value, null);
        } catch (final NumberFormatException ignored) {
            return new ParseResult<>(null, label + " must be a number");
        }
    }

    @SafeVarargs
    private static <T> List<String> errors(final ParseResult<T>... results) {
        final var errors = new ArrayList<String>();
        for (final var result : results) {
            if (result.error() != null) errors.add(result.error());
        }
        return errors;
    }

    private static String formatNumber(final double value) {
        return value == Math.rint(value)
                ? String.format(Locale.ROOT, "%.0f", value)
                : Double.toString(value);
    }

    private record ParseResult<T>(@Nullable T value, @Nullable String error) {
    }

    private record WarpForm(String name, String world, String x, String y, String z, String pitch, String yaw) {
        private static WarpForm from(final DialogResponseView response) {
            return new WarpForm(
                    text(response, "name"),
                    text(response, "world"),
                    text(response, "x"),
                    text(response, "y"),
                    text(response, "z"),
                    text(response, "pitch"),
                    text(response, "yaw")
            );
        }
    }
}
