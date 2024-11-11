package net.thenextlvl.tweaks.model;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import java.util.Set;

@Getter
@NullMarked
@Accessors(fluent = true, chain = false)
public class CommandConfig {
    private @SerializedName("day") CommandDefinition day = new CommandDefinition("day");
    private @SerializedName("midnight") CommandDefinition midnight = new CommandDefinition("midnight");
    private @SerializedName("night") CommandDefinition night = new CommandDefinition("night");
    private @SerializedName("noon") CommandDefinition noon = new CommandDefinition("noon");
    private @SerializedName("time") CommandDefinition time = new CommandDefinition("time");

    private @SerializedName("rain") CommandDefinition rain = new CommandDefinition("rain");
    private @SerializedName("sun") CommandDefinition sun = new CommandDefinition("sun");
    private @SerializedName("thunder") CommandDefinition thunder = new CommandDefinition("thunder");
    private @SerializedName("weather") CommandDefinition weather = new CommandDefinition("weather");

    private @SerializedName("delete-home") CommandDefinition deleteHome = new CommandDefinition("delete-home", "delhome");
    private @SerializedName("home") CommandDefinition home = new CommandDefinition("home");
    private @SerializedName("homes") CommandDefinition homes = new CommandDefinition("homes");
    private @SerializedName("set-home") CommandDefinition setHome = new CommandDefinition("set-home", "sethome");

    private @SerializedName("enchant") CommandDefinition enchant = new CommandDefinition("enchant");
    private @SerializedName("head") CommandDefinition head = new CommandDefinition("head", "skull");
    private @SerializedName("item") CommandDefinition item = new CommandDefinition("item", "i");
    private @SerializedName("lore") CommandDefinition lore = new CommandDefinition("lore");
    private @SerializedName("rename") CommandDefinition rename = new CommandDefinition("rename");
    private @SerializedName("repair") CommandDefinition repair = new CommandDefinition("repair");
    private @SerializedName("unbreakable") CommandDefinition unbreakable = new CommandDefinition("unbreakable");
    private @SerializedName("unenchant") CommandDefinition unenchant = new CommandDefinition("unenchant");

    private @SerializedName("msg") CommandDefinition msg = new CommandDefinition("msg", "tell", "write", "t", "w");
    private @SerializedName("msg-toggle") CommandDefinition msgToggle = new CommandDefinition("msgtoggle", "togglemsg");
    private @SerializedName("reply") CommandDefinition reply = new CommandDefinition("reply", "r");

    private @SerializedName("back") CommandDefinition back = new CommandDefinition("back");
    private @SerializedName("enderchest") CommandDefinition enderchest = new CommandDefinition("enderchest", "ec");
    private @SerializedName("feed") CommandDefinition feed = new CommandDefinition("feed");
    private @SerializedName("fly") CommandDefinition fly = new CommandDefinition("fly", "flight");
    private @SerializedName("gamemode") CommandDefinition gamemode = new CommandDefinition("gamemode", "gm");
    private @SerializedName("god") CommandDefinition god = new CommandDefinition("god", "invincible");
    private @SerializedName("hat") CommandDefinition hat = new CommandDefinition("hat");
    private @SerializedName("heal") CommandDefinition heal = new CommandDefinition("heal");
    private @SerializedName("inventory") CommandDefinition inventory = new CommandDefinition("inventory", "invsee", "inv");
    private @SerializedName("offline-teleport") CommandDefinition offlineTeleport = new CommandDefinition("offline-teleport", "offline-tp", "tpo");
    private @SerializedName("ping") CommandDefinition ping = new CommandDefinition("ping", "latency", "ms");
    private @SerializedName("seen") CommandDefinition seen = new CommandDefinition("seen", "find");
    private @SerializedName("speed") CommandDefinition speed = new CommandDefinition("speed");
    private @SerializedName("suicide") CommandDefinition suicide = new CommandDefinition("suicide");
    private @SerializedName("vanish") CommandDefinition vanish = new CommandDefinition("vanish", "v", "invisible");

    private @SerializedName("broadcast") CommandDefinition broadcast = new CommandDefinition("broadcast", "bc");
    private @SerializedName("lobby") CommandDefinition lobby = new CommandDefinition("lobby", isProxyEnabled(), "hub", "l");
    private @SerializedName("motd") CommandDefinition motd = new CommandDefinition("motd");

    private @SerializedName("discord") CommandDefinition discord = new CommandDefinition("discord", "dc");
    private @SerializedName("reddit") CommandDefinition reddit = new CommandDefinition("reddit");
    private @SerializedName("teamspeak") CommandDefinition teamspeak = new CommandDefinition("teamspeak", "teamspeak3", "ts", "ts3");
    private @SerializedName("tiktok") CommandDefinition tiktok = new CommandDefinition("tiktok");
    private @SerializedName("twitch") CommandDefinition twitch = new CommandDefinition("twitch");
    private @SerializedName("website") CommandDefinition website = new CommandDefinition("website");
    private @SerializedName("x") CommandDefinition x = new CommandDefinition("x", "twitter");
    private @SerializedName("youtube") CommandDefinition youtube = new CommandDefinition("youtube", "yt");

    private @SerializedName("set-spawn") CommandDefinition setSpawn = new CommandDefinition("set-spawn", "setspawn");
    private @SerializedName("spawn") CommandDefinition spawn = new CommandDefinition("spawn");

    private @SerializedName("teleport-accept") CommandDefinition teleportAccept = new CommandDefinition("tpaccept");
    private @SerializedName("teleport-ask") CommandDefinition teleportAsk = new CommandDefinition("tpa", "tpask");
    private @SerializedName("teleport-deny") CommandDefinition teleportDeny = new CommandDefinition("tpadeny", "tpdeny");
    private @SerializedName("teleport-here") CommandDefinition teleportHere = new CommandDefinition("tpahere", "tphere");
    private @SerializedName("teleport-toggle") CommandDefinition teleportToggle = new CommandDefinition("tpatoggle", "toggletpa");

    private @SerializedName("delete-warp") CommandDefinition deleteWarp = new CommandDefinition("delete-warp", "delwarp");
    private @SerializedName("set-warp") CommandDefinition setWarp = new CommandDefinition("set-warp", "setwarp");
    private @SerializedName("warp") CommandDefinition warp = new CommandDefinition("warp");
    private @SerializedName("warps") CommandDefinition warps = new CommandDefinition("warps");

    private @SerializedName("anvil") CommandDefinition anvil = new CommandDefinition("anvil");
    private @SerializedName("cartography-table") CommandDefinition cartographyTable = new CommandDefinition("cartography-table", "cartography");
    private @SerializedName("enchanting-table") CommandDefinition enchantingTable = new CommandDefinition("enchanting-table", "enchanting");
    private @SerializedName("grindstone") CommandDefinition grindstone = new CommandDefinition("grindstone");
    private @SerializedName("loom") CommandDefinition loom = new CommandDefinition("loom");
    private @SerializedName("smithing-table") CommandDefinition smithingTable = new CommandDefinition("smithing-table", "smithing");
    private @SerializedName("stonecutter") CommandDefinition stonecutter = new CommandDefinition("stonecutter");
    private @SerializedName("workbench") CommandDefinition workbench = new CommandDefinition("workbench", "wb");

    private boolean isProxyEnabled() {
        var plugin = JavaPlugin.getPlugin(TweaksPlugin.class);
        return plugin.getServer().spigot().getPaperConfig().getBoolean("proxies.velocity.enabled")
               || plugin.getServer().spigot().getSpigotConfig().getBoolean("settings.bungeecord");
    }

    @Getter
    @Accessors(fluent = true, chain = false)
    public static class CommandDefinition {
        private @SerializedName("command") String command;
        private @SerializedName("aliases") Set<String> aliases;
        private @SerializedName("enabled") boolean enabled;

        public CommandDefinition(String command, boolean enabled, String... aliases) {
            this.aliases = Set.of(aliases);
            this.command = command;
            this.enabled = enabled;
        }

        public CommandDefinition(String command, String... aliases) {
            this(command, true, aliases);
        }
    }
}
