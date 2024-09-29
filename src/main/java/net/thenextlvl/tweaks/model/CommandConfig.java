package net.thenextlvl.tweaks.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Set;

@Getter
@Accessors(fluent = true, chain = false)
public class CommandConfig {
    private @SerializedName("delete-home") CommandDefinition deleteHome = new CommandDefinition("delete-home", Set.of("delhome"));
    private @SerializedName("home") CommandDefinition home = new CommandDefinition("home", Set.of());
    private @SerializedName("homes") CommandDefinition homes = new CommandDefinition("homes", Set.of());
    private @SerializedName("set-home") CommandDefinition setHome = new CommandDefinition("set-home", Set.of("sethome"));

    private @SerializedName("set-spawn") CommandDefinition setSpawn = new CommandDefinition("set-spawn", Set.of("setspawn"));
    private @SerializedName("spawn") CommandDefinition spawn = new CommandDefinition("spawn", Set.of());

    private @SerializedName("lobby") CommandDefinition lobby = new CommandDefinition("lobby", Set.of("hub", "l"));

    private @SerializedName("teleport-accept") CommandDefinition teleportAccept = new CommandDefinition("tpaccept", Set.of());
    private @SerializedName("teleport-ask") CommandDefinition teleportAsk = new CommandDefinition("tpa", Set.of("tpask"));
    private @SerializedName("teleport-deny") CommandDefinition teleportDeny = new CommandDefinition("tpadeny", Set.of("tpdeny"));
    private @SerializedName("teleport-here") CommandDefinition teleportHere = new CommandDefinition("tpahere", Set.of("tphere"));
    private @SerializedName("teleport-toggle") CommandDefinition teleportToggle = new CommandDefinition("tpatoggle", Set.of("toggletpa"));

    private @SerializedName("msg") CommandDefinition msg = new CommandDefinition("msg", Set.of("tell", "write", "t", "w"));
    private @SerializedName("msg-toggle") CommandDefinition msgToggle = new CommandDefinition("msgtoggle", Set.of("togglemsg"));
    private @SerializedName("reply") CommandDefinition reply = new CommandDefinition("reply", Set.of("r"));

    private @SerializedName("delete-warp") CommandDefinition deleteWarp = new CommandDefinition("delete-warp", Set.of("delwarp"));
    private @SerializedName("set-warp") CommandDefinition setWarp = new CommandDefinition("set-warp", Set.of("setwarp"));
    private @SerializedName("warp") CommandDefinition warp = new CommandDefinition("warp", Set.of());
    private @SerializedName("warps") CommandDefinition warps = new CommandDefinition("warps", Set.of());

    private @SerializedName("discord") CommandDefinition discord = new CommandDefinition("discord", Set.of("dc"));
    private @SerializedName("reddit") CommandDefinition reddit = new CommandDefinition("reddit", Set.of());
    private @SerializedName("teamspeak") CommandDefinition teamspeak = new CommandDefinition("teamspeak", Set.of("teamspeak3", "ts", "ts3"));
    private @SerializedName("twitch") CommandDefinition twitch = new CommandDefinition("twitch", Set.of());
    private @SerializedName("website") CommandDefinition website = new CommandDefinition("website", Set.of());
    private @SerializedName("x") CommandDefinition x = new CommandDefinition("x", Set.of("twitter"));
    private @SerializedName("youtube") CommandDefinition youtube = new CommandDefinition("youtube", Set.of("yt"));

    @Getter
    @AllArgsConstructor
    @Accessors(fluent = true, chain = false)
    public static class CommandDefinition {
        private @SerializedName("command") String command;
        private @SerializedName("aliases") Set<String> aliases;
    }
}
