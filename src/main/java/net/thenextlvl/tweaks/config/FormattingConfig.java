package net.thenextlvl.tweaks.config;

import com.google.gson.annotations.SerializedName;

public record FormattingConfig(
        @SerializedName("broadcast-header") String broadcastHeader,
        @SerializedName("broadcast-format") String broadcastFormat,
        @SerializedName("broadcast-footer") String broadcastFooter,
        @SerializedName("chat-format") String chatFormat
) {
}
