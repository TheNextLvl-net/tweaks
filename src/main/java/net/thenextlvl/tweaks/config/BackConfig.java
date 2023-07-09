package net.thenextlvl.tweaks.config;

import com.google.gson.annotations.SerializedName;

public record BackConfig(
        @SerializedName("buffer-stack-size") int bufferStackSize
) {
}
