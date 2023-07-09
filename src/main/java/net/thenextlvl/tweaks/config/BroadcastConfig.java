package net.thenextlvl.tweaks.config;

import com.google.gson.annotations.SerializedName;

public record BroadcastConfig(
        @SerializedName("header") String header,
        @SerializedName("format") String format,
        @SerializedName("footer") String footer
) {
}
