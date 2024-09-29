package net.thenextlvl.tweaks.gui;

import core.paper.gui.PagedGUI;
import core.paper.item.ActionItem;
import core.paper.item.ItemBuilder;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.model.NamedLocation;
import net.thenextlvl.tweaks.model.PluginConfig;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
public abstract class NamedLocationGUI extends PagedGUI<TweaksPlugin, NamedLocation> {
    private static final List<Material> materials = Arrays.stream(Material.values())
            .filter(material -> !material.isLegacy())
            .filter(Material::isItem)
            .toList();
    private final Collection<NamedLocation> elements;
    private final Options options;

    public NamedLocationGUI(TweaksPlugin plugin, PluginConfig.GUIConfig.GUI config, Player owner, Component title, Collection<NamedLocation> elements) {
        super(plugin, owner, title, config.rows());
        this.elements = elements;
        this.options = new Options(
                config.actionSlots(),
                config.buttonSlotPrevious(),
                config.buttonSlotNext()
        );
        loadPage(getCurrentPage());
    }

    @Override
    public ActionItem constructItem(NamedLocation element) {
        var translation = "gui.item.location";
        return new ItemBuilder(getIcon(element))
                .itemName(plugin.bundle().component(owner, translation,
                        Placeholder.parsed("name", element.getName())))
                .withAction(() -> {
                    teleport(element);
                    close();
                });
    }

    private Material getIcon(NamedLocation element) {
        var material = plugin.config().guis().nameIcons().get(element.getName());
        if (material != null) return material;
        var hash = Math.abs(element.getName().hashCode());
        return materials.get(hash % materials.size());
    }

    protected abstract void teleport(NamedLocation element);

    @Override
    public Component getPageFormat(int page) {
        var key = page > getCurrentPage() ? "gui.page.next" : "gui.page.previous";
        return plugin.bundle().component(owner, key);
    }
}
