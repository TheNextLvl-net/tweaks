package net.thenextlvl.tweaks.config;

import com.google.gson.annotations.SerializedName;

public record TweaksConfig(
        @SerializedName("back-config") BackConfig backConfig,
        @SerializedName("broadcast-config") BroadcastConfig broadcastConfig,
        @SerializedName("inventory-config") InventoryConfig inventoryConfig,
        @SerializedName("vanilla-tweaks") VanillaTweaks vanillaTweaks
) {
}
