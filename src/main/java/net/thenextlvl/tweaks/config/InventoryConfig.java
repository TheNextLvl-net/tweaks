package net.thenextlvl.tweaks.config;

import com.google.gson.annotations.SerializedName;

public record InventoryConfig(
        @SerializedName("helmet") ConfigItem helmet,
        @SerializedName("chestplate") ConfigItem chestplate,
        @SerializedName("leggings") ConfigItem leggings,
        @SerializedName("boots") ConfigItem boots,
        @SerializedName("off-hand") ConfigItem offHand,
        @SerializedName("cursor") ConfigItem cursor,
        @SerializedName("placeholder") ConfigItem placeholder,
        @SerializedName("update-time") long updateTime
) {
}
