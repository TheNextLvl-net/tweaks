package net.thenextlvl.tweaks.config;

import com.google.gson.annotations.SerializedName;

public record TweaksConfig(
        @SerializedName("general-config") GeneralConfig generalConfig,
        @SerializedName("formatting-config") FormattingConfig formattingConfig,
        @SerializedName("inventory-config") InventoryConfig inventoryConfig,
        @SerializedName("vanilla-tweaks") VanillaTweaks vanillaTweaks
) {
}
