package net.thenextlvl.tweaks.config;

import com.google.gson.annotations.SerializedName;

public record ServerConfig(
        @SerializedName("enable-lobby-command") boolean enableLobbyCommand,
        @SerializedName("lobby-server-name") String lobbyServerName
) {
}
