package net.thenextlvl.tweaks;

import com.google.gson.GsonBuilder;
import core.file.FileIO;
import core.file.format.GsonFile;
import core.i18n.file.ComponentBundle;
import core.io.IO;
import core.paper.adapters.inventory.MaterialAdapter;
import core.paper.adapters.world.LocationAdapter;
import core.paper.adapters.world.WorldAdapter;
import core.paper.messenger.PluginMessenger;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.thenextlvl.tweaks.command.environment.time.*;
import net.thenextlvl.tweaks.command.environment.weather.RainCommand;
import net.thenextlvl.tweaks.command.environment.weather.SunCommand;
import net.thenextlvl.tweaks.command.environment.weather.ThunderCommand;
import net.thenextlvl.tweaks.command.environment.weather.WeatherCommand;
import net.thenextlvl.tweaks.command.home.DeleteHomeCommand;
import net.thenextlvl.tweaks.command.home.HomeCommand;
import net.thenextlvl.tweaks.command.home.HomesCommand;
import net.thenextlvl.tweaks.command.home.SetHomeCommand;
import net.thenextlvl.tweaks.command.item.*;
import net.thenextlvl.tweaks.command.message.MSGCommand;
import net.thenextlvl.tweaks.command.message.MSGToggleCommand;
import net.thenextlvl.tweaks.command.message.ReplyCommand;
import net.thenextlvl.tweaks.command.player.*;
import net.thenextlvl.tweaks.command.server.BroadcastCommand;
import net.thenextlvl.tweaks.command.server.LobbyCommand;
import net.thenextlvl.tweaks.command.server.MotdCommand;
import net.thenextlvl.tweaks.command.social.*;
import net.thenextlvl.tweaks.command.spawn.SetSpawnCommand;
import net.thenextlvl.tweaks.command.spawn.SpawnCommand;
import net.thenextlvl.tweaks.command.tpa.*;
import net.thenextlvl.tweaks.command.warp.DeleteWarpCommand;
import net.thenextlvl.tweaks.command.warp.SetWarpCommand;
import net.thenextlvl.tweaks.command.warp.WarpCommand;
import net.thenextlvl.tweaks.command.warp.WarpsCommand;
import net.thenextlvl.tweaks.command.workstation.*;
import net.thenextlvl.tweaks.controller.*;
import net.thenextlvl.tweaks.listener.*;
import net.thenextlvl.tweaks.model.CommandConfig;
import net.thenextlvl.tweaks.model.MessageMigrator;
import net.thenextlvl.tweaks.model.PluginConfig;
import net.thenextlvl.tweaks.version.PluginVersionChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.ServerLinks;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import static org.bukkit.ServerLinks.Type.*;

@NullMarked
public class TweaksPlugin extends JavaPlugin {
    private final Metrics metrics = new Metrics(this, 19651);
    private final PluginMessenger messenger = new PluginMessenger(this);
    private final PluginVersionChecker versionChecker = new PluginVersionChecker(this);

    private final CommandConfig commands = new GsonFile<>(
            IO.of(getDataFolder(), "commands.json"),
            new CommandConfig()
    ).validate().save().getRoot();

    private @Nullable ComponentBundle bundle;
    private @Nullable FileIO<PluginConfig> config;
    private @Nullable ServiceController serviceController;

    private final BackController backController = new BackController(this);
    private final DataController dataController = new DataController(this);
    private final HomeController homeController = new HomeController(this);
    private final MSGController msgController = new MSGController();
    private final TPAController tpaController = new TPAController(this);
    private final TeleportController teleportController = new TeleportController(this);
    private final WarpController warpController = new WarpController(this);

    @Override
    public void onLoad() {
        versionChecker.checkVersion();
    }

    @Override
    public void onEnable() {
        initConfigs();
        initTranslations();
        initControllers();
        initMotd();
        registerCommands();
        registerLinks();
        registerListeners();
    }

    @Override
    public void onDisable() {
        metrics.shutdown();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new BackListener(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new ConnectionListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityListener(this), this);
        getServer().getPluginManager().registerEvents(new SpawnListener(this), this);
        getServer().getPluginManager().registerEvents(new WorldListener(this), this);
    }

    private void registerCommands() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(event -> {
            var registrar = event.registrar();
            var features = config().features;
            registerItemCommands(registrar);
            registerPlayerCommands(registrar);
            registerServerCommands(registrar);
            if (features.time) registerTimeCommands(registrar);
            if (features.weather) registerWeatherCommands(registrar);
            if (features.workstation) registerWorkstationCommands(registrar);
            if (features.homes) registerHomeCommands(registrar);
            if (features.msg) registerMSGCommands(registrar);
            if (features.spawn) registerSpawnCommands(registrar);
            if (features.tpa) registerTpaCommands(registrar);
            if (features.warps) registerWarpCommands(registrar);
            if (features.social.linkCommands) registerLinkCommands(registrar);
        }));
    }

    private void registerTimeCommands(Commands registrar) {
        if (commands().day.enabled) new DayCommand(this).register(registrar);
        if (commands().midnight.enabled) new MidnightCommand(this).register(registrar);
        if (commands().night.enabled) new NightCommand(this).register(registrar);
        if (commands().noon.enabled) new NoonCommand(this).register(registrar);
        if (commands().time.enabled) new TimeCommand(this).register(registrar);
    }

    private void registerWeatherCommands(Commands registrar) {
        if (commands().rain.enabled) new RainCommand(this).register(registrar);
        if (commands().sun.enabled) new SunCommand(this).register(registrar);
        if (commands().thunder.enabled) new ThunderCommand(this).register(registrar);
        if (commands().weather.enabled) new WeatherCommand(this).register(registrar);
    }

    private void registerItemCommands(Commands registrar) {
        if (commands().enchant.enabled) new EnchantCommand(this).register(registrar);
        if (commands().head.enabled) new HeadCommand(this).register(registrar);
        if (commands().item.enabled) new ItemCommand(this).register(registrar);
        if (commands().lore.enabled) new LoreCommand(this).register(registrar);
        if (commands().rename.enabled) new RenameCommand(this).register(registrar);
        if (commands().repair.enabled) new RepairCommand(this).register(registrar);
        if (commands().unbreakable.enabled) new UnbreakableCommand(this).register(registrar);
        if (commands().unenchant.enabled) new UnenchantCommand(this).register(registrar);
    }

    private void registerPlayerCommands(Commands registrar) {
        if (commands().back.enabled) new BackCommand(this).register(registrar);
        if (commands().enderchest.enabled) new EnderChestCommand(this).register(registrar);
        if (commands().feed.enabled) new FeedCommand(this).register(registrar);
        if (commands().fly.enabled) new FlyCommand(this).register(registrar);
        if (commands().gamemode.enabled) new GameModeCommand(this).register(registrar);
        if (commands().god.enabled) new GodCommand(this).register(registrar);
        if (commands().hat.enabled) new HatCommand(this).register(registrar);
        if (commands().heal.enabled) new HealCommand(this).register(registrar);
        if (commands().inventory.enabled) new InventoryCommand(this).register(registrar);
        if (commands().offlineTeleport.enabled) new OfflineTeleportCommand(this).register(registrar);
        if (commands().ping.enabled) new PingCommand(this).register(registrar);
        if (commands().seen.enabled) new SeenCommand(this).register(registrar);
        if (commands().speed.enabled) new SpeedCommand(this).register(registrar);
        if (commands().suicide.enabled) new SuicideCommand(this).register(registrar);
        if (commands().trash.enabled) new TrashCommand(this).register(registrar);
        if (commands().vanish.enabled) new VanishCommand(this).register(registrar);
    }

    private void registerServerCommands(Commands registrar) {
        if (commands().back.enabled) new BroadcastCommand(this).register(registrar);
        if (commands().lobby.enabled) new LobbyCommand(this).register(registrar);
        if (commands().motd.enabled) new MotdCommand(this).register(registrar);
    }

    private void registerWorkstationCommands(Commands registrar) {
        if (commands().anvil.enabled) new AnvilCommand(this).register(registrar);
        if (commands().cartographyTable.enabled) new CartographyTableCommand(this).register(registrar);
        if (commands().enchantingTable.enabled) new EnchantingTableCommand(this).register(registrar);
        if (commands().grindstone.enabled) new GrindstoneCommand(this).register(registrar);
        if (commands().loom.enabled) new LoomCommand(this).register(registrar);
        if (commands().smithingTable.enabled) new SmithingTableCommand(this).register(registrar);
        if (commands().stonecutter.enabled) new StonecutterCommand(this).register(registrar);
        if (commands().workbench.enabled) new WorkbenchCommand(this).register(registrar);
    }

    private void registerHomeCommands(Commands registrar) {
        if (commands().deleteHome.enabled) new DeleteHomeCommand(this).register(registrar);
        if (commands().home.enabled) new HomeCommand(this).register(registrar);
        if (commands().homes.enabled) new HomesCommand(this).register(registrar);
        if (commands().setHome.enabled) new SetHomeCommand(this).register(registrar);
    }

    private void registerMSGCommands(Commands registrar) {
        if (commands().msg.enabled) new MSGCommand(this).register(registrar);
        if (commands().msgToggle.enabled) new MSGToggleCommand(this).register(registrar);
        if (commands().reply.enabled) new ReplyCommand(this).register(registrar);
    }

    private void registerSpawnCommands(Commands registrar) {
        if (commands().setSpawn.enabled) new SetSpawnCommand(this).register(registrar);
        if (commands().spawn.enabled) new SpawnCommand(this).register(registrar);
    }

    private void registerTpaCommands(Commands registrar) {
        if (commands().teleportAccept.enabled) new TPAcceptCommand(this).register(registrar);
        if (commands().teleportAsk.enabled) new TPAskCommand(this).register(registrar);
        if (commands().teleportDeny.enabled) new TPADenyCommand(this).register(registrar);
        if (commands().teleportHere.enabled) new TPAHereCommand(this).register(registrar);
        if (commands().teleportToggle.enabled) new TPAToggleCommand(this).register(registrar);
    }

    private void registerWarpCommands(Commands registrar) {
        if (commands().deleteWarp.enabled) new DeleteWarpCommand(this).register(registrar);
        if (commands().setWarp.enabled) new SetWarpCommand(this).register(registrar);
        if (commands().warp.enabled) new WarpCommand(this).register(registrar);
        if (commands().warps.enabled) new WarpsCommand(this).register(registrar);
    }

    private void registerLinkCommands(Commands registrar) {
        if (commands().discord.enabled) new DiscordCommand(this).register(registrar);
        if (commands().reddit.enabled) new RedditCommand(this).register(registrar);
        if (commands().teamspeak.enabled) new TeamSpeakCommand(this).register(registrar);
        if (commands().tiktok.enabled) new TikTokCommand(this).register(registrar);
        if (commands().twitch.enabled) new TwitchCommand(this).register(registrar);
        if (commands().website.enabled) new WebsiteCommand(this).register(registrar);
        if (commands().x.enabled) new XCommand(this).register(registrar);
        if (commands().youtube.enabled) new YouTubeCommand(this).register(registrar);
    }

    private void registerLinks() {
        var social = config().features.social;
        if (!social.serverLinks) return;

        if (social.discord) registerLink("url.discord", config().links.discord);
        if (social.reddit) registerLink("url.reddit", config().links.reddit);
        if (social.teamspeak) registerLink("url.teamspeak", config().links.teamspeak);
        if (social.tiktok) registerLink("url.tiktok", config().links.tiktok);
        if (social.twitch) registerLink("url.twitch", config().links.twitch);
        if (social.x) registerLink("url.x", config().links.x);
        if (social.youtube) registerLink("url.youtube", config().links.youtube);

        if (social.announcements) registerLink(ANNOUNCEMENTS, config().links.announcements);
        if (social.community) registerLink(COMMUNITY, config().links.community);
        if (social.feedback) registerLink(FEEDBACK, config().links.feedback);
        if (social.forum) registerLink(FORUMS, config().links.forum);
        if (social.guidelines) registerLink(COMMUNITY_GUIDELINES, config().links.guidelines);
        if (social.issues) registerLink(REPORT_BUG, config().links.issues);
        if (social.news) registerLink(NEWS, config().links.news);
        if (social.status) registerLink(STATUS, config().links.status);
        if (social.support) registerLink(SUPPORT, config().links.support);
        if (social.website) registerLink(WEBSITE, config().links.website);
    }

    private void registerLink(ServerLinks.Type type, String string) {
        if (!string.isBlank()) getServer().getServerLinks().addLink(type, URI.create(string));
    }

    private void registerLink(String translationKey, String string) {
        var translation = bundle().translate(translationKey, Locale.US);
        if (translation == null || string.isBlank()) return;
        getServer().getServerLinks().addLink(translation, URI.create(string));
    }

    public void saveConfig() {
        if (config != null) config.save();
    }

    private void initConfigs() {
        this.config = new GsonFile<>(
                IO.of(getDataFolder(), "config.json"),
                new PluginConfig(), new GsonBuilder()
                .registerTypeHierarchyAdapter(Location.class, new LocationAdapter.Simple())
                .registerTypeHierarchyAdapter(Material.class, new MaterialAdapter())
                .registerTypeHierarchyAdapter(World.class, new WorldAdapter.Key())
                .setPrettyPrinting()
                .serializeNulls()
                .create()
        ).validate().save();
    }

    private void initTranslations() {
        this.bundle = ComponentBundle.builder(
                        Key.key("tweaks", "translations"),
                        getDataPath().resolve("translations")
                ).miniMessage(MiniMessage.builder().tags(TagResolver.resolver(
                        TagResolver.standard(),
                        Placeholder.component("prefix", Component.translatable("prefix")),
                        Placeholder.parsed("announcements", config().links.announcements),
                        Placeholder.parsed("community", config().links.community),
                        Placeholder.parsed("discord", config().links.discord),
                        Placeholder.parsed("feedback", config().links.feedback),
                        Placeholder.parsed("forum", config().links.forum),
                        Placeholder.parsed("guidelines", config().links.guidelines),
                        Placeholder.parsed("issues", config().links.issues),
                        Placeholder.parsed("news", config().links.news),
                        Placeholder.parsed("reddit", config().links.reddit),
                        Placeholder.parsed("status", config().links.status),
                        Placeholder.parsed("support", config().links.support),
                        Placeholder.parsed("teamspeak", config().links.teamspeak),
                        Placeholder.parsed("tiktok", config().links.tiktok),
                        Placeholder.parsed("twitch", config().links.twitch),
                        Placeholder.parsed("website", config().links.website),
                        Placeholder.parsed("x", config().links.x),
                        Placeholder.parsed("youtube", config().links.youtube)
                )).build())
                .resource("tweaks.properties", Locale.US)
                .resource("tweaks_german.properties", Locale.GERMANY)
                .migrator(new MessageMigrator())
                .build().registerTranslations();
    }

    private void initControllers() {
        if (getServer().getPluginManager().isPluginEnabled("ServiceIO"))
            this.serviceController = new ServiceController(this);
    }

    private void initMotd() {
        var motd = config().general.motd;
        if (motd != null) getServer().motd(MiniMessage.miniMessage().deserialize(motd));
    }

    public PluginMessenger messenger() {
        return messenger;
    }

    public BackController backController() {
        return backController;
    }

    public DataController dataController() {
        return dataController;
    }

    public HomeController homeController() {
        return homeController;
    }

    public MSGController msgController() {
        return msgController;
    }

    public TPAController tpaController() {
        return tpaController;
    }

    public TeleportController teleportController() {
        return teleportController;
    }

    public WarpController warpController() {
        return warpController;
    }

    public CommandConfig commands() {
        return commands;
    }

    public ComponentBundle bundle() {
        return Objects.requireNonNull(bundle, "Bundle not initialized yet!");
    }

    public PluginConfig config() {
        return Objects.requireNonNull(config, "Config not initialized yet!").getRoot();
    }

    public @Nullable ServiceController serviceController() {
        return serviceController;
    }

    public TagResolver.Builder serviceResolvers(Player player) {
        var resolver = TagResolver.builder().resolvers(
                Placeholder.component("custom_name", Optional.ofNullable(player.customName())
                        .orElseGet(player::name)),
                Placeholder.component("display_name", player.displayName()),
                Placeholder.parsed("balance", ""),
                Placeholder.parsed("balance_unformatted", ""),
                Placeholder.parsed("bank_balance", ""),
                Placeholder.parsed("bank_balance_unformatted", ""),
                Placeholder.parsed("chat_display_name", player.getName()),
                Placeholder.parsed("chat_prefix", ""),
                Placeholder.parsed("chat_suffix", ""),
                Placeholder.parsed("currency_name", ""),
                Placeholder.parsed("currency_name_plural", ""),
                Placeholder.parsed("currency_symbol", ""),
                Placeholder.parsed("group", ""),
                Placeholder.parsed("group_prefix", ""),
                Placeholder.parsed("group_suffix", ""),
                Placeholder.parsed("language_tag", player.locale().toLanguageTag()),
                Placeholder.parsed("locale", player.locale().getDisplayName(player.locale())),
                Placeholder.parsed("player", player.getName()),
                Placeholder.parsed("world", player.getWorld().getName())
        );
        if (serviceController == null) return resolver;
        return resolver.resolvers(serviceController.serviceResolvers(player));
    }
}
