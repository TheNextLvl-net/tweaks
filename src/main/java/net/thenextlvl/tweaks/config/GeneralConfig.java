package net.thenextlvl.tweaks.config;

import com.google.gson.annotations.SerializedName;

public record GeneralConfig(
        @SerializedName("back-buffer-stack-size") int backBufferStackSize,
        @SerializedName("default-permission-level") int defaultPermissionLevel,
        @SerializedName("override-join-message") boolean overrideJoinMessage,
        @SerializedName("override-quit-message") boolean overrideQuitMessage,
        @SerializedName("override-chat") boolean overrideChat,
        @SerializedName("log-chat") boolean logChat
) {
}
