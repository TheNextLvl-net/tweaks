package net.thenextlvl.tweaks.model;

import com.google.gson.annotations.SerializedName;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import java.util.Set;

@NullMarked
public final class CommandConfig {
    public @SerializedName("day") CommandDefinition day = new CommandDefinition("day");
    public @SerializedName("midnight") CommandDefinition midnight = new CommandDefinition("midnight");
    public @SerializedName("night") CommandDefinition night = new CommandDefinition("night");
    public @SerializedName("noon") CommandDefinition noon = new CommandDefinition("noon");
    public @SerializedName("time") CommandDefinition time = new CommandDefinition("time");

    public @SerializedName("rain") CommandDefinition rain = new CommandDefinition("rain");
    public @SerializedName("sun") CommandDefinition sun = new CommandDefinition("sun");
    public @SerializedName("thunder") CommandDefinition thunder = new CommandDefinition("thunder");
    public @SerializedName("weather") CommandDefinition weather = new CommandDefinition("weather");

    public @SerializedName("delete-home") CommandDefinition deleteHome = new CommandDefinition("delete-home", "delhome");
    public @SerializedName("home") CommandDefinition home = new CommandDefinition("home");
    public @SerializedName("homes") CommandDefinition homes = new CommandDefinition("homes");
    public @SerializedName("set-home") CommandDefinition setHome = new CommandDefinition("set-home", "sethome");

    public @SerializedName("enchant") CommandDefinition enchant = new CommandDefinition("enchant");
    public @SerializedName("head") CommandDefinition head = new CommandDefinition("head", "skull");
    public @SerializedName("item") CommandDefinition item = new CommandDefinition("item", "i");
    public @SerializedName("lore") CommandDefinition lore = new CommandDefinition("lore");
    public @SerializedName("rename") CommandDefinition rename = new CommandDefinition("rename");
    public @SerializedName("repair") CommandDefinition repair = new CommandDefinition("repair");
    public @SerializedName("unbreakable") CommandDefinition unbreakable = new CommandDefinition("unbreakable");
    public @SerializedName("unenchant") CommandDefinition unenchant = new CommandDefinition("unenchant");

    public @SerializedName("msg") CommandDefinition msg = new CommandDefinition("msg", "tell", "write", "t", "w");
    public @SerializedName("msg-toggle") CommandDefinition msgToggle = new CommandDefinition("msgtoggle", "togglemsg");
    public @SerializedName("reply") CommandDefinition reply = new CommandDefinition("reply", "r");

    public @SerializedName("back") CommandDefinition back = new CommandDefinition("back");
    public @SerializedName("enderchest") CommandDefinition enderchest = new CommandDefinition("enderchest", "ec");
    public @SerializedName("feed") CommandDefinition feed = new CommandDefinition("feed");
    public @SerializedName("fly") CommandDefinition fly = new CommandDefinition("fly", "flight");
    public @SerializedName("gamemode") CommandDefinition gamemode = new CommandDefinition("gamemode", "gm");
    public @SerializedName("god") CommandDefinition god = new CommandDefinition("god", "invincible");
    public @SerializedName("hat") CommandDefinition hat = new CommandDefinition("hat");
    public @SerializedName("heal") CommandDefinition heal = new CommandDefinition("heal");
    public @SerializedName("inventory") CommandDefinition inventory = new CommandDefinition("inventory", "invsee", "inv");
    public @SerializedName("offline-teleport") CommandDefinition offlineTeleport = new CommandDefinition("offline-teleport", "offline-tp", "tpo");
    public @SerializedName("ping") CommandDefinition ping = new CommandDefinition("ping", "latency", "ms");
    public @SerializedName("seen") CommandDefinition seen = new CommandDefinition("seen", "find");
    public @SerializedName("speed") CommandDefinition speed = new CommandDefinition("speed");
    public @SerializedName("suicide") CommandDefinition suicide = new CommandDefinition("suicide");
    public @SerializedName("trash") CommandDefinition trash = new CommandDefinition("trash", "dispose", "garbage");
    public @SerializedName("vanish") CommandDefinition vanish = new CommandDefinition("vanish", "v", "invisible");

    public @SerializedName("broadcast") CommandDefinition broadcast = new CommandDefinition("broadcast", "bc");
    public @SerializedName("lobby") CommandDefinition lobby = new CommandDefinition("lobby", isProxyEnabled(), "hub", "l");
    public @SerializedName("motd") CommandDefinition motd = new CommandDefinition("motd");

    public @SerializedName("discord") CommandDefinition discord = new CommandDefinition("discord", "dc");
    public @SerializedName("reddit") CommandDefinition reddit = new CommandDefinition("reddit");
    public @SerializedName("teamspeak") CommandDefinition teamspeak = new CommandDefinition("teamspeak", "teamspeak3", "ts", "ts3");
    public @SerializedName("tiktok") CommandDefinition tiktok = new CommandDefinition("tiktok");
    public @SerializedName("twitch") CommandDefinition twitch = new CommandDefinition("twitch");
    public @SerializedName("website") CommandDefinition website = new CommandDefinition("website");
    public @SerializedName("x") CommandDefinition x = new CommandDefinition("x", "twitter");
    public @SerializedName("youtube") CommandDefinition youtube = new CommandDefinition("youtube", "yt");

    public @SerializedName("set-spawn") CommandDefinition setSpawn = new CommandDefinition("set-spawn", "setspawn");
    public @SerializedName("spawn") CommandDefinition spawn = new CommandDefinition("spawn");

    public @SerializedName("teleport-accept") CommandDefinition teleportAccept = new CommandDefinition("tpaccept");
    public @SerializedName("teleport-ask") CommandDefinition teleportAsk = new CommandDefinition("tpa", "tpask");
    public @SerializedName("teleport-deny") CommandDefinition teleportDeny = new CommandDefinition("tpadeny", "tpdeny");
    public @SerializedName("teleport-here") CommandDefinition teleportHere = new CommandDefinition("tpahere", "tphere");
    public @SerializedName("teleport-toggle") CommandDefinition teleportToggle = new CommandDefinition("tpatoggle", "toggletpa");

    public @SerializedName("delete-warp") CommandDefinition deleteWarp = new CommandDefinition("delete-warp", "delwarp");
    public @SerializedName("set-warp") CommandDefinition setWarp = new CommandDefinition("set-warp", "setwarp");
    public @SerializedName("warp") CommandDefinition warp = new CommandDefinition("warp");
    public @SerializedName("warps") CommandDefinition warps = new CommandDefinition("warps");

    public @SerializedName("anvil") CommandDefinition anvil = new CommandDefinition("anvil");
    public @SerializedName("cartography-table") CommandDefinition cartographyTable = new CommandDefinition("cartography-table", "cartography");
    public @SerializedName("enchanting-table") CommandDefinition enchantingTable = new CommandDefinition("enchanting-table", "enchanting");
    public @SerializedName("grindstone") CommandDefinition grindstone = new CommandDefinition("grindstone");
    public @SerializedName("loom") CommandDefinition loom = new CommandDefinition("loom");
    public @SerializedName("smithing-table") CommandDefinition smithingTable = new CommandDefinition("smithing-table", "smithing");
    public @SerializedName("stonecutter") CommandDefinition stonecutter = new CommandDefinition("stonecutter");
    public @SerializedName("workbench") CommandDefinition workbench = new CommandDefinition("workbench", "wb");

    private boolean isProxyEnabled() {
        var plugin = JavaPlugin.getPlugin(TweaksPlugin.class);
        return plugin.getServer().spigot().getPaperConfig().getBoolean("proxies.velocity.enabled")
               || plugin.getServer().spigot().getSpigotConfig().getBoolean("settings.bungeecord");
    }

    public static class CommandDefinition {
        public @SerializedName("command") String command;
        public @SerializedName("aliases") Set<String> aliases;
        public @SerializedName("enabled") boolean enabled;

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
