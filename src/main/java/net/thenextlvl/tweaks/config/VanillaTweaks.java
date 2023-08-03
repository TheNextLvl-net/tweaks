package net.thenextlvl.tweaks.config;

import com.google.gson.annotations.SerializedName;

public record VanillaTweaks(
        @SerializedName("cow-milking-cooldown") long cowMilkingCooldown,
        @SerializedName("mushroom-stew-cooldown") long mushroomStewCooldown,
        @SerializedName("sheep-wool-growth-cooldown") long sheepWoolGrowthCooldown,
        @SerializedName("animal-heal-by-feeding") boolean animalHealByFeeding
) {
}
