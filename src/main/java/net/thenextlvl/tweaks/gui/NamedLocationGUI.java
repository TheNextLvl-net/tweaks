package net.thenextlvl.tweaks.gui;

import core.paper.gui.PaginatedGUI;
import core.paper.item.ActionItem;
import core.paper.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.Argument;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.model.NamedLocation;
import net.thenextlvl.tweaks.model.PluginConfig;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@NullMarked
public abstract class NamedLocationGUI extends PaginatedGUI<TweaksPlugin, NamedLocation> {
    private static final List<Material> materials = Arrays.stream(Material.values())
            .filter(material -> !material.isLegacy())
            .filter(Material::isItem)
            .toList();
    private final Collection<NamedLocation> elements;
    private final Pagination pagination;

    public NamedLocationGUI(TweaksPlugin plugin, PluginConfig.GUIConfig.GUI config, Player owner, Component title, Collection<NamedLocation> elements) {
        super(plugin, owner, title, config.rows);
        this.elements = elements;
        this.pagination = new Pagination(
                config.actionSlots,
                config.buttonSlotPrevious,
                config.buttonSlotNext
        );
        loadPage(getCurrentPage());
    }

    @Override
    public Collection<NamedLocation> getElements() {
        return elements;
    }

    @Override
    public Pagination getPagination() {
        return pagination;
    }

    @Override
    public ActionItem constructItem(NamedLocation element) {
        var translation = "gui.item.location";
        return ItemBuilder.of(getIcon(element))
                .itemName(plugin.bundle().component(translation, owner,
                        Argument.string("name", element.getName())))
                .withAction(() -> {
                    teleport(element);
                    close();
                });
    }

    private Material getIcon(NamedLocation element) {
        var material = plugin.config().guis.nameIcons.get(element.getName());
        if (material != null) return material;
        var hash = Math.abs(element.getName().hashCode());
        return materials.get(hash % materials.size());
    }

    protected abstract void teleport(NamedLocation element);

    @Override
    public Component getPageFormat(int page) {
        var key = page > getCurrentPage() ? "gui.page.next" : "gui.page.previous";
        return plugin.bundle().component(key, owner);
    }
}
