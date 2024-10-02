package net.thenextlvl.tweaks.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Set;

@Getter
@Accessors(fluent = true, chain = false)
public class CommandConfig {
    private @SerializedName("day") CommandDefinition day = new CommandDefinition("day", Set.of());
    private @SerializedName("midnight") CommandDefinition midnight = new CommandDefinition("midnight", Set.of());
    private @SerializedName("night") CommandDefinition night = new CommandDefinition("night", Set.of());
    private @SerializedName("noon") CommandDefinition noon = new CommandDefinition("noon", Set.of());
    private @SerializedName("time") CommandDefinition time = new CommandDefinition("time", Set.of());

    private @SerializedName("rain") CommandDefinition rain = new CommandDefinition("rain", Set.of());
    private @SerializedName("sun") CommandDefinition sun = new CommandDefinition("sun", Set.of());
    private @SerializedName("thunder") CommandDefinition thunder = new CommandDefinition("thunder", Set.of());
    private @SerializedName("weather") CommandDefinition weather = new CommandDefinition("weather", Set.of());

    private @SerializedName("delete-home") CommandDefinition deleteHome = new CommandDefinition("delete-home", Set.of("delhome"));
    private @SerializedName("home") CommandDefinition home = new CommandDefinition("home", Set.of());
    private @SerializedName("homes") CommandDefinition homes = new CommandDefinition("homes", Set.of());
    private @SerializedName("set-home") CommandDefinition setHome = new CommandDefinition("set-home", Set.of("sethome"));

    private @SerializedName("enchant") CommandDefinition enchant = new CommandDefinition("enchant", Set.of());
    private @SerializedName("head") CommandDefinition head = new CommandDefinition("head", Set.of("skull"));
    private @SerializedName("item") CommandDefinition item = new CommandDefinition("item", Set.of("i"));
    private @SerializedName("lore") CommandDefinition lore = new CommandDefinition("lore", Set.of());
    private @SerializedName("rename") CommandDefinition rename = new CommandDefinition("rename", Set.of());
    private @SerializedName("repair") CommandDefinition repair = new CommandDefinition("repair", Set.of());
    private @SerializedName("unbreakable") CommandDefinition unbreakable = new CommandDefinition("unbreakable", Set.of());
    private @SerializedName("unenchant") CommandDefinition unenchant = new CommandDefinition("unenchant", Set.of());

    private @SerializedName("msg") CommandDefinition msg = new CommandDefinition("msg", Set.of("tell", "write", "t", "w"));
    private @SerializedName("msg-toggle") CommandDefinition msgToggle = new CommandDefinition("msgtoggle", Set.of("togglemsg"));
    private @SerializedName("reply") CommandDefinition reply = new CommandDefinition("reply", Set.of("r"));

    private @SerializedName("back") CommandDefinition back = new CommandDefinition("back", Set.of());
    private @SerializedName("enderchest") CommandDefinition enderchest = new CommandDefinition("enderchest", Set.of("ec"));
    private @SerializedName("feed") CommandDefinition feed = new CommandDefinition("feed", Set.of());
    private @SerializedName("fly") CommandDefinition fly = new CommandDefinition("fly", Set.of("flight"));
    private @SerializedName("gamemode") CommandDefinition gamemode = new CommandDefinition("gamemode", Set.of("gm"));
    private @SerializedName("god") CommandDefinition god = new CommandDefinition("god", Set.of("invincible"));
    private @SerializedName("hat") CommandDefinition hat = new CommandDefinition("hat", Set.of());
    private @SerializedName("heal") CommandDefinition heal = new CommandDefinition("heal", Set.of());
    private @SerializedName("inventory") CommandDefinition inventory = new CommandDefinition("inventory", Set.of("invsee", "inv"));
    private @SerializedName("offline-teleport") CommandDefinition offlineTeleport = new CommandDefinition("offline-teleport", Set.of("offline-tp", "otp", "tpo"));
    private @SerializedName("ping") CommandDefinition ping = new CommandDefinition("ping", Set.of("latency", "ms"));
    private @SerializedName("seen") CommandDefinition seen = new CommandDefinition("seen", Set.of("find"));
    private @SerializedName("speed") CommandDefinition speed = new CommandDefinition("speed", Set.of());
    private @SerializedName("suicide") CommandDefinition suicide = new CommandDefinition("suicide", Set.of());
    private @SerializedName("vanish") CommandDefinition vanish = new CommandDefinition("vanish", Set.of("v", "invisible"));

    private @SerializedName("broadcast") CommandDefinition broadcast = new CommandDefinition("broadcast", Set.of("bc"));
    private @SerializedName("lobby") CommandDefinition lobby = new CommandDefinition("lobby", Set.of("hub", "l"));
    private @SerializedName("motd") CommandDefinition motd = new CommandDefinition("motd", Set.of());

    private @SerializedName("discord") CommandDefinition discord = new CommandDefinition("discord", Set.of("dc"));
    private @SerializedName("reddit") CommandDefinition reddit = new CommandDefinition("reddit", Set.of());
    private @SerializedName("teamspeak") CommandDefinition teamspeak = new CommandDefinition("teamspeak", Set.of("teamspeak3", "ts", "ts3"));
    private @SerializedName("twitch") CommandDefinition twitch = new CommandDefinition("twitch", Set.of());
    private @SerializedName("website") CommandDefinition website = new CommandDefinition("website", Set.of());
    private @SerializedName("x") CommandDefinition x = new CommandDefinition("x", Set.of("twitter"));
    private @SerializedName("youtube") CommandDefinition youtube = new CommandDefinition("youtube", Set.of("yt"));

    private @SerializedName("set-spawn") CommandDefinition setSpawn = new CommandDefinition("set-spawn", Set.of("setspawn"));
    private @SerializedName("spawn") CommandDefinition spawn = new CommandDefinition("spawn", Set.of());

    private @SerializedName("teleport-accept") CommandDefinition teleportAccept = new CommandDefinition("tpaccept", Set.of());
    private @SerializedName("teleport-ask") CommandDefinition teleportAsk = new CommandDefinition("tpa", Set.of("tpask"));
    private @SerializedName("teleport-deny") CommandDefinition teleportDeny = new CommandDefinition("tpadeny", Set.of("tpdeny"));
    private @SerializedName("teleport-here") CommandDefinition teleportHere = new CommandDefinition("tpahere", Set.of("tphere"));
    private @SerializedName("teleport-toggle") CommandDefinition teleportToggle = new CommandDefinition("tpatoggle", Set.of("toggletpa"));

    private @SerializedName("delete-warp") CommandDefinition deleteWarp = new CommandDefinition("delete-warp", Set.of("delwarp"));
    private @SerializedName("set-warp") CommandDefinition setWarp = new CommandDefinition("set-warp", Set.of("setwarp"));
    private @SerializedName("warp") CommandDefinition warp = new CommandDefinition("warp", Set.of());
    private @SerializedName("warps") CommandDefinition warps = new CommandDefinition("warps", Set.of());

    private @SerializedName("anvil") CommandDefinition anvil = new CommandDefinition("anvil", Set.of());
    private @SerializedName("cartography-table") CommandDefinition cartographyTable = new CommandDefinition("cartography-table", Set.of("cartography"));
    private @SerializedName("enchanting-table") CommandDefinition enchantingTable = new CommandDefinition("enchanting-table", Set.of("enchanting"));
    private @SerializedName("grindstone") CommandDefinition grindstone = new CommandDefinition("grindstone", Set.of());
    private @SerializedName("loom") CommandDefinition loom = new CommandDefinition("loom", Set.of());
    private @SerializedName("smithing-table") CommandDefinition smithingTable = new CommandDefinition("smithing-table", Set.of("smithing"));
    private @SerializedName("stonecutter") CommandDefinition stonecutter = new CommandDefinition("stonecutter", Set.of());
    private @SerializedName("workbench") CommandDefinition workbench = new CommandDefinition("workbench", Set.of("wb"));

    @Getter
    @AllArgsConstructor
    @Accessors(fluent = true, chain = false)
    public static class CommandDefinition {
        private @SerializedName("command") String command;
        private @SerializedName("aliases") Set<String> aliases;
    }
}
