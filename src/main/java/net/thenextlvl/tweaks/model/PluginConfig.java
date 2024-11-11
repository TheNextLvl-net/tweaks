package net.thenextlvl.tweaks.model;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Getter
@NullMarked
@Accessors(fluent = true, chain = false)
public class PluginConfig {
    private @SerializedName("features") FeatureConfig features = new FeatureConfig();
    private @SerializedName("general") GeneralConfig general = new GeneralConfig();
    private @SerializedName("guis") GUIConfig guis = new GUIConfig();
    private @SerializedName("links") LinkConfig links = new LinkConfig();
    private @SerializedName("spawn") SpawnConfig spawn = new SpawnConfig();
    private @SerializedName("homes") HomeConfig homes = new HomeConfig();
    private @SerializedName("teleportation") TeleportConfig teleport = new TeleportConfig();
    private @SerializedName("animals") AnimalConfig vanilla = new AnimalConfig();

    @Getter
    @Accessors(fluent = true, chain = false)
    public static class GeneralConfig {
        private @SerializedName("message-deletion-timeout") long messageDeletionTimeout = TimeUnit.MINUTES.toMillis(10);
        private @SerializedName("back-buffer-stack-size") int backBufferStackSize = 5;
        private @SerializedName("default-permission-level") byte defaultPermissionLevel = -1;
        private @SerializedName("enchantment-overflow") boolean enchantmentOverflow = false;
        private @SerializedName("override-join-message") boolean overrideJoinMessage = true;
        private @SerializedName("override-quit-message") boolean overrideQuitMessage = true;
        private @SerializedName("override-chat") boolean overrideChat = true;
        private @SerializedName("log-chat") boolean logChat = true;

        @Setter
        private @SerializedName("lobby-server-name") String lobbyServerName = "lobby";

        @Setter
        @Nullable
        private @SerializedName("motd") String motd = null;
    }

    @Getter
    @Accessors(fluent = true, chain = false)
    public static class AnimalConfig {
        private @SerializedName("cow-milking-cooldown") long cowMilkingCooldown = 0;
        private @SerializedName("mushroom-stew-cooldown") long mushroomStewCooldown = 0;
        private @SerializedName("sheep-wool-growth-cooldown") long sheepWoolGrowthCooldown = 0;
        private @SerializedName("animal-heal-by-feeding") boolean animalHealByFeeding = false;
    }

    @Getter
    @Accessors(fluent = true, chain = false)
    public static class HomeConfig {
        /**
         * Represents the maximum number of homes allowed.
         * A value less than 0 indicates an unlimited number of homes.
         */
        private @SerializedName("limit") int limit = 5;
        private @SerializedName("unnamed-name") String unnamedName = "home";
    }

    @Getter
    @Accessors(fluent = true, chain = false)
    public static class SpawnConfig {
        private @SerializedName("teleport-on-first-join") boolean teleportOnFirstJoin = true;
        private @SerializedName("teleport-on-join") boolean teleportOnJoin = true;
        private @SerializedName("teleport-on-respawn") boolean teleportOnRespawn = true;
        private @SerializedName("ignore-respawn-position") boolean ignoreRespawnPosition = false;

        @Setter
        @Nullable
        private @SerializedName("location") Location location = null;
    }

    @Getter
    @Accessors(fluent = true, chain = false)
    public static class GUIConfig {
        private @SerializedName("inventory") InventoryGUI inventory = new InventoryGUI();

        private @SerializedName("homes") GUI homes = new GUI();
        private @SerializedName("warps") GUI warps = new GUI();

        private @SerializedName("name-icons") Map<String, Material> nameIcons = Map.of(
                "Farmworld", Material.FARMLAND,
                "Nether", Material.NETHERRACK,
                "End", Material.DRAGON_EGG,
                "Spawn", Material.COMPASS
        );

        @Getter
        @Accessors(fluent = true, chain = false)
        public static class InventoryGUI {
            private @SerializedName("helmet") Material helmet = Material.LIME_STAINED_GLASS_PANE;
            private @SerializedName("chestplate") Material chestplate = Material.LIME_STAINED_GLASS_PANE;
            private @SerializedName("leggings") Material leggings = Material.LIME_STAINED_GLASS_PANE;
            private @SerializedName("boots") Material boots = Material.LIME_STAINED_GLASS_PANE;
            private @SerializedName("off-hand") Material offHand = Material.LIGHT_BLUE_STAINED_GLASS_PANE;
            private @SerializedName("cursor") Material cursor = Material.CYAN_STAINED_GLASS_PANE;
            private @SerializedName("placeholder") Material placeholder = Material.IRON_BARS;
            private @SerializedName("update-time") long updateTime = 20;
        }

        @Getter
        @Accessors(fluent = true, chain = false)
        public static class GUI {
            private @SerializedName("enabled") boolean enabled = true;
            private @SerializedName("rows") int rows = 5;
            private @SerializedName("action-slots") int[] actionSlots = new int[]{
                    10, 11, 12, 13, 14, 15, 16,
                    19, 20, 21, 22, 23, 24, 25,
                    28, 29, 30, 31, 32, 33, 34,
            };
            private @SerializedName("button-slot-next") int buttonSlotNext = 41;
            private @SerializedName("button-slot-previous") int buttonSlotPrevious = 39;
        }
    }

    @Getter
    @Accessors(fluent = true, chain = false)
    public static class TeleportConfig {
        private @SerializedName("cooldown") long cooldown = TimeUnit.SECONDS.toMillis(3);
        private @SerializedName("cooldown-allow-movement") boolean allowMovement = false;
        private @SerializedName("tpa-timeout") long tpaTimeout = TimeUnit.SECONDS.toMillis(30);
    }

    @Getter
    @Accessors(fluent = true, chain = false)
    public static class FeatureConfig {
        private @SerializedName("homes") boolean homes = true;
        private @SerializedName("msg") boolean msg = true;
        private @SerializedName("spawn") boolean spawn = true;
        private @SerializedName("tpa") boolean tpa = true;
        private @SerializedName("warps") boolean warps = true;

        private @SerializedName("social") SocialConfig social = new SocialConfig();

        @Getter
        @Accessors(fluent = true, chain = false)
        public static class SocialConfig {
            private @SerializedName("add-link-commands") boolean linkCommands = true;
            private @SerializedName("add-server-links") boolean serverLinks = true;

            private @SerializedName("announcements") boolean announcements = true;
            private @SerializedName("community") boolean community = true;
            private @SerializedName("feedback") boolean feedback = true;
            private @SerializedName("forum") boolean forum = true;
            private @SerializedName("guidelines") boolean guidelines = true;
            private @SerializedName("issues") boolean issues = true;
            private @SerializedName("news") boolean news = true;
            private @SerializedName("status") boolean status = true;
            private @SerializedName("support") boolean support = true;
            private @SerializedName("website") boolean website = true;

            private @SerializedName("discord") boolean discord = true;
            private @SerializedName("reddit") boolean reddit = true;
            private @SerializedName("teamspeak") boolean teamspeak = true;
            private @SerializedName("twitch") boolean twitch = true;
            private @SerializedName("x") boolean x = true;
            private @SerializedName("youtube") boolean youtube = true;
        }
    }

    @Getter
    @Accessors(fluent = true, chain = false)
    public static class LinkConfig {
        private @SerializedName("announcements") String announcements = "https://announcements.example.com";
        private @SerializedName("community") String community = "https://community.example.com";
        private @SerializedName("feedback") String feedback = "https://feedback.example.com";
        private @SerializedName("forum") String forum = "https://forum.example.com";
        private @SerializedName("guidelines") String guidelines = "https://guidelines.example.com";
        private @SerializedName("issues") String issues = "https://issues.example.com";
        private @SerializedName("news") String news = "https://news.example.com";
        private @SerializedName("status") String status = "https://status.example.com";
        private @SerializedName("support") String support = "https://support.example.com";
        private @SerializedName("website") String website = "https://example.com";

        private @SerializedName("discord") String discord = "https://discord.gg/invite/example";
        private @SerializedName("reddit") String reddit = "https://www.reddit.com/r/example";
        private @SerializedName("teamspeak") String teamspeak = "teamspeak.example.com";
        private @SerializedName("twitch") String twitch = "https://www.twitch.tv/example";
        private @SerializedName("x") String x = "https://x.com/example";
        private @SerializedName("youtube") String youtube = "https://www.youtube.com/example";
    }
}
