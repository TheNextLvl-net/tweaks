package net.thenextlvl.tweaks.model;

import com.google.gson.annotations.SerializedName;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@NullMarked
public class PluginConfig {
    public @SerializedName("features") FeatureConfig features = new FeatureConfig();
    public @SerializedName("general") GeneralConfig general = new GeneralConfig();
    public @SerializedName("guis") GUIConfig guis = new GUIConfig();
    public @SerializedName("spawn") SpawnConfig spawn = new SpawnConfig();
    public @SerializedName("homes") HomeConfig homes = new HomeConfig();
    public @SerializedName("teleportation") TeleportConfig teleport = new TeleportConfig();
    public @SerializedName("animals") AnimalConfig vanilla = new AnimalConfig();
    
    public @SerializedName("links") Map<String, String> links = new HashMap<>(LINK_DEFAULTS);

    public static class GeneralConfig {
        public @SerializedName("message-deletion-timeout") long messageDeletionTimeout = TimeUnit.MINUTES.toMillis(10);
        public @SerializedName("back-buffer-stack-size") int backBufferStackSize = 5;
        public @SerializedName("default-permission-level") byte defaultPermissionLevel = -1;
        public @SerializedName("enchantment-overflow") boolean enchantmentOverflow = false;
        public @SerializedName("override-join-message") boolean overrideJoinMessage = true;
        public @SerializedName("override-quit-message") boolean overrideQuitMessage = true;
        public @SerializedName("override-chat") boolean overrideChat = true;
        public @SerializedName("log-chat") boolean logChat = true;

        public @SerializedName("lobby-server-name") String lobbyServerName = "lobby";

        @Nullable
        public @SerializedName("motd") String motd = null;
    }

    public static class AnimalConfig {
        public @SerializedName("cow-milking-cooldown") long cowMilkingCooldown = 0;
        public @SerializedName("mushroom-stew-cooldown") long mushroomStewCooldown = 0;
        public @SerializedName("sheep-wool-growth-cooldown") long sheepWoolGrowthCooldown = 0;
        public @SerializedName("animal-heal-by-feeding") boolean animalHealByFeeding = false;
    }

    public static class HomeConfig {
        /**
         * Represents the maximum number of homes allowed.
         * A value less than 0 indicates an unlimited number of homes.
         */
        public @SerializedName("limit") int limit = 5;
        public @SerializedName("unnamed-name") String unnamedName = "home";
    }

    public static class SpawnConfig {
        public @SerializedName("teleport-on-first-join") boolean teleportOnFirstJoin = true;
        public @SerializedName("teleport-on-join") boolean teleportOnJoin = true;
        public @SerializedName("teleport-on-respawn") boolean teleportOnRespawn = true;
        public @SerializedName("ignore-respawn-position") boolean ignoreRespawnPosition = false;

        @Nullable
        public @SerializedName("location") Location location = null;
    }

    public static class GUIConfig {
        public @SerializedName("inventory") InventoryGUI inventory = new InventoryGUI();

        public @SerializedName("homes") GUI homes = new GUI();
        public @SerializedName("warps") GUI warps = new GUI();

        public @SerializedName("name-icons") Map<String, Material> nameIcons = Map.of(
                "Farmworld", Material.FARMLAND,
                "Nether", Material.NETHERRACK,
                "End", Material.DRAGON_EGG,
                "Spawn", Material.COMPASS
        );

        public static class InventoryGUI {
            public @SerializedName("helmet") Material helmet = Material.LIME_STAINED_GLASS_PANE;
            public @SerializedName("chestplate") Material chestplate = Material.LIME_STAINED_GLASS_PANE;
            public @SerializedName("leggings") Material leggings = Material.LIME_STAINED_GLASS_PANE;
            public @SerializedName("boots") Material boots = Material.LIME_STAINED_GLASS_PANE;
            public @SerializedName("off-hand") Material offHand = Material.LIGHT_BLUE_STAINED_GLASS_PANE;
            public @SerializedName("cursor") Material cursor = Material.CYAN_STAINED_GLASS_PANE;
            public @SerializedName("placeholder") Material placeholder = Material.IRON_BARS;
            public @SerializedName("update-time") long updateTime = 20;
        }

        public static class GUI {
            public @SerializedName("enabled") boolean enabled = true;
            public @SerializedName("rows") int rows = 5;
            public @SerializedName("action-slots") int[] actionSlots = new int[]{
                    10, 11, 12, 13, 14, 15, 16,
                    19, 20, 21, 22, 23, 24, 25,
                    28, 29, 30, 31, 32, 33, 34,
            };
            public @SerializedName("button-slot-next") int buttonSlotNext = 41;
            public @SerializedName("button-slot-previous") int buttonSlotPrevious = 39;
        }
    }

    public static class TeleportConfig {
        public @SerializedName("cooldown") long cooldown = TimeUnit.SECONDS.toMillis(3);
        public @SerializedName("cooldown-allow-movement") boolean allowMovement = false;
        public @SerializedName("tpa-timeout") long tpaTimeout = TimeUnit.SECONDS.toMillis(30);
    }

    public static class FeatureConfig {
        public @SerializedName("homes") boolean homes = true;
        public @SerializedName("msg") boolean msg = true;
        public @SerializedName("spawn") boolean spawn = true;
        public @SerializedName("tpa") boolean tpa = true;
        public @SerializedName("warps") boolean warps = true;
        public @SerializedName("workstation") boolean workstation = true;
        public @SerializedName("weather") boolean weather = true;
        public @SerializedName("time") boolean time = true;

        public @SerializedName("social") SocialConfig social = new SocialConfig();

        public static class SocialConfig {
            public @SerializedName("add-link-commands") boolean linkCommands = true;
            public @SerializedName("add-server-links") boolean serverLinks = true;

            public @SerializedName("announcements") boolean announcements = true;
            public @SerializedName("community") boolean community = true;
            public @SerializedName("feedback") boolean feedback = true;
            public @SerializedName("forum") boolean forum = true;
            public @SerializedName("guidelines") boolean guidelines = true;
            public @SerializedName("issues") boolean issues = true;
            public @SerializedName("news") boolean news = true;
            public @SerializedName("status") boolean status = true;
            public @SerializedName("support") boolean support = true;
            public @SerializedName("website") boolean website = true;

            public @SerializedName("discord") boolean discord = true;
            public @SerializedName("reddit") boolean reddit = true;
            public @SerializedName("teamspeak") boolean teamspeak = true;
            public @SerializedName("tiktok") boolean tiktok = true;
            public @SerializedName("twitch") boolean twitch = true;
            public @SerializedName("x") boolean x = true;
            public @SerializedName("youtube") boolean youtube = true;
        }
    }

    public static final Map<String, String> LINK_DEFAULTS = Map.ofEntries(
            Map.entry("announcements", "https://announcements.example.com"),
            Map.entry("community", "https://community.example.com"),
            Map.entry("discord", "https://discord.gg/invite/example"),
            Map.entry("feedback", "https://feedback.example.com"),
            Map.entry("forum", "https://forum.example.com"),
            Map.entry("guidelines", "https://guidelines.example.com"),
            Map.entry("issues", "https://issues.example.com"),
            Map.entry("news", "https://news.example.com"),
            Map.entry("reddit", "https://www.reddit.com/r/example"),
            Map.entry("status", "https://status.example.com"),
            Map.entry("support", "https://support.example.com"),
            Map.entry("teamspeak", "teamspeak.example.com"),
            Map.entry("tiktok", "https://www.tiktok.com/@example"),
            Map.entry("twitch", "https://www.twitch.tv/example"),
            Map.entry("website", "https://example.com"),
            Map.entry("x", "https://x.com/example"),
            Map.entry("youtube", "https://www.youtube.com/example")
    );
}
