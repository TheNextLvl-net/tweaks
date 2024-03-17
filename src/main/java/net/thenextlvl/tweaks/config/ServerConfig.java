package net.thenextlvl.tweaks.config;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

@Setter
@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public class ServerConfig {
    @SerializedName("enable-lobby-command")
    private final boolean enableLobbyCommand;
    @SerializedName("lobby-server-name")
    private final String lobbyServerName;
    @SerializedName("motd")
    private @Nullable String motd;
}
