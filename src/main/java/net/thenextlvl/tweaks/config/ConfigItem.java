package net.thenextlvl.tweaks.config;

import com.google.gson.annotations.SerializedName;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public record ConfigItem(
        @SerializedName("icon") Material icon,
        @SerializedName("title") String title
) {
    public ItemStack serialize() {
        var placeholder = new ItemStack(icon());
        placeholder.editMeta(meta -> meta.displayName(Component.text(title())));
        return placeholder;
    }
}
