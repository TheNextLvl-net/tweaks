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
import lombok.Getter;
import lombok.experimental.Accessors;
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

import java.io.File;
import java.net.URI;
import java.util.Locale;
import java.util.Objects;

import static org.bukkit.ServerLinks.Type.*;

@Getter
@NullMarked
@Accessors(fluent = true)
@SuppressWarnings("UnstableApiUsage")
public class TweaksPlugin extends JavaPlugin {
    private final Metrics metrics = new Metrics(this, 19651);
    private final PluginMessenger messenger = new PluginMessenger(this);
    private final PluginVersionChecker versionChecker = new PluginVersionChecker(this);

    private final CommandConfig commands = new GsonFile<>(
            IO.of(getDataFolder(), "commands.json"),
            new CommandConfig()
    ).validate().saveIfAbsent().getRoot();

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
        versionChecker().checkVersion();
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
        metrics().shutdown();
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
            var features = config().features();
            registerItemCommands(registrar);
            registerPlayerCommands(registrar);
            registerServerCommands(registrar);
            if (features.time()) registerTimeCommands(registrar);
            if (features.weather()) registerWeatherCommands(registrar);
            if (features.workstation()) registerWorkstationCommands(registrar);
            if (features.homes()) registerHomeCommands(registrar);
            if (features.msg()) registerMSGCommands(registrar);
            if (features.spawn()) registerSpawnCommands(registrar);
            if (features.tpa()) registerTpaCommands(registrar);
            if (features.warps()) registerWarpCommands(registrar);
            if (features.social().linkCommands()) registerLinkCommands(registrar);
        }));
    }

    private void registerTimeCommands(Commands registrar) {
        new DayCommand(this).register(registrar);
        new MidnightCommand(this).register(registrar);
        new NightCommand(this).register(registrar);
        new NoonCommand(this).register(registrar);
        new TimeCommand(this).register(registrar);
    }

    private void registerWeatherCommands(Commands registrar) {
        new RainCommand(this).register(registrar);
        new SunCommand(this).register(registrar);
        new ThunderCommand(this).register(registrar);
        new WeatherCommand(this).register(registrar);
    }

    private void registerItemCommands(Commands registrar) {
        new EnchantCommand(this).register(registrar);
        new HeadCommand(this).register(registrar);
        new ItemCommand(this).register(registrar);
        new LoreCommand(this).register(registrar);
        new RenameCommand(this).register(registrar);
        new RepairCommand(this).register(registrar);
        new UnbreakableCommand(this).register(registrar);
        new UnenchantCommand(this).register(registrar);
    }

    private void registerPlayerCommands(Commands registrar) {
        new BackCommand(this).register(registrar);
        new EnderChestCommand(this).register(registrar);
        new FeedCommand(this).register(registrar);
        new FlyCommand(this).register(registrar);
        new GameModeCommand(this).register(registrar);
        new GodCommand(this).register(registrar);
        new HatCommand(this).register(registrar);
        new HealCommand(this).register(registrar);
        new InventoryCommand(this).register(registrar);
        new OfflineTeleportCommand(this).register(registrar);
        new PingCommand(this).register(registrar);
        new SeenCommand(this).register(registrar);
        new SpeedCommand(this).register(registrar);
        new SuicideCommand(this).register(registrar);
        new VanishCommand(this).register(registrar);
    }

    private void registerServerCommands(Commands registrar) {
        new BroadcastCommand(this).register(registrar);
        if (config().features().lobby())
            new LobbyCommand(this).register(registrar);
        new MotdCommand(this).register(registrar);
    }

    private void registerWorkstationCommands(Commands registrar) {
        new AnvilCommand(this).register(registrar);
        new CartographyTableCommand(this).register(registrar);
        new EnchantingTableCommand(this).register(registrar);
        new GrindstoneCommand(this).register(registrar);
        new LoomCommand(this).register(registrar);
        new SmithingTableCommand(this).register(registrar);
        new StonecutterCommand(this).register(registrar);
        new WorkbenchCommand(this).register(registrar);
    }

    private void registerHomeCommands(Commands registrar) {
        new DeleteHomeCommand(this).register(registrar);
        new HomeCommand(this).register(registrar);
        new HomesCommand(this).register(registrar);
        new SetHomeCommand(this).register(registrar);
    }

    private void registerMSGCommands(Commands registrar) {
        new MSGCommand(this).register(registrar);
        new MSGToggleCommand(this).register(registrar);
        new ReplyCommand(this).register(registrar);
    }

    private void registerSpawnCommands(Commands registrar) {
        new SetSpawnCommand(this).register(registrar);
        new SpawnCommand(this).register(registrar);
    }

    private void registerTpaCommands(Commands registrar) {
        new TPADenyCommand(this).register(registrar);
        new TPAHereCommand(this).register(registrar);
        new TPAcceptCommand(this).register(registrar);
        new TPAskCommand(this).register(registrar);
        new TPAToggleCommand(this).register(registrar);
    }

    private void registerWarpCommands(Commands registrar) {
        new DeleteWarpCommand(this).register(registrar);
        new SetWarpCommand(this).register(registrar);
        new WarpCommand(this).register(registrar);
        new WarpsCommand(this).register(registrar);
    }

    private void registerLinkCommands(Commands registrar) {
        var social = config().features().social();
        if (social.discord()) new DiscordCommand(this).register(registrar);
        if (social.reddit()) new RedditCommand(this).register(registrar);
        if (social.teamspeak()) new TeamSpeakCommand(this).register(registrar);
        if (social.twitch()) new TwitchCommand(this).register(registrar);
        if (social.tiktok()) new TikTokCommand(this).register(registrar);
        if (social.website()) new WebsiteCommand(this).register(registrar);
        if (social.x()) new XCommand(this).register(registrar);
        if (social.youtube()) new YouTubeCommand(this).register(registrar);
    }

    private void registerLinks() {
        var social = config().features().social();
        if (!social.serverLinks()) return;

        if (social.discord()) registerLink("url.discord", config().links().discord());
        if (social.reddit()) registerLink("url.reddit", config().links().reddit());
        if (social.tiktok()) registerLink("url.tiktok", config().links().tiktok());
        if (social.twitch()) registerLink("url.twitch", config().links().twitch());
        if (social.x()) registerLink("url.x", config().links().x());
        if (social.youtube()) registerLink("url.youtube", config().links().youtube());

        if (social.announcements()) registerLink(ANNOUNCEMENTS, config().links().announcements());
        if (social.community()) registerLink(COMMUNITY, config().links().community());
        if (social.feedback()) registerLink(FEEDBACK, config().links().feedback());
        if (social.forum()) registerLink(FORUMS, config().links().forum());
        if (social.guidelines()) registerLink(COMMUNITY_GUIDELINES, config().links().guidelines());
        if (social.issues()) registerLink(REPORT_BUG, config().links().issues());
        if (social.news()) registerLink(NEWS, config().links().news());
        if (social.status()) registerLink(STATUS, config().links().status());
        if (social.support()) registerLink(SUPPORT, config().links().support());
        if (social.website()) registerLink(WEBSITE, config().links().website());
    }

    private void registerLink(ServerLinks.Type type, String string) {
        if (!string.isBlank()) getServer().getServerLinks().addLink(type, URI.create(string));
    }

    private void registerLink(String translation, String string) {
        if (string.isBlank()) return;
        getServer().getServerLinks().addLink(bundle().component(Locale.US, translation), URI.create(string));
    }

    public void saveConfig() {
        if (config != null) config.save();
    }

    private void initConfigs() {
        this.config = new GsonFile<>(
                IO.of(getDataFolder(), "config.json"),
                new PluginConfig(), new GsonBuilder()
                .registerTypeHierarchyAdapter(Location.class, LocationAdapter.simple())
                .registerTypeHierarchyAdapter(Material.class, MaterialAdapter.instance())
                .registerTypeHierarchyAdapter(World.class, WorldAdapter.key())
                .setPrettyPrinting()
                .serializeNulls()
                .create()
        ).validate().save();
    }

    private void initTranslations() {
        this.bundle = new ComponentBundle(new File(getDataFolder(), "translations"),
                audience -> audience instanceof Player player ? player.locale() : Locale.US)
                .register("tweaks", Locale.US)
                .register("tweaks_german", Locale.GERMANY)
                .miniMessage(bundle -> MiniMessage.builder().tags(TagResolver.resolver(
                        TagResolver.standard(),
                        Placeholder.component("prefix", bundle.component(Locale.US, "prefix")),
                        Placeholder.parsed("announcements", config().links().announcements()),
                        Placeholder.parsed("community", config().links().community()),
                        Placeholder.parsed("discord", config().links().discord()),
                        Placeholder.parsed("feedback", config().links().feedback()),
                        Placeholder.parsed("forum", config().links().forum()),
                        Placeholder.parsed("guidelines", config().links().guidelines()),
                        Placeholder.parsed("issues", config().links().issues()),
                        Placeholder.parsed("news", config().links().news()),
                        Placeholder.parsed("reddit", config().links().reddit()),
                        Placeholder.parsed("status", config().links().status()),
                        Placeholder.parsed("support", config().links().support()),
                        Placeholder.parsed("teamspeak", config().links().teamspeak()),
                        Placeholder.parsed("tiktok", config().links().tiktok()),
                        Placeholder.parsed("twitch", config().links().twitch()),
                        Placeholder.parsed("website", config().links().website()),
                        Placeholder.parsed("x", config().links().x()),
                        Placeholder.parsed("youtube", config().links().youtube())
                )).build());
    }

    private void initControllers() {
        if (getServer().getPluginManager().isPluginEnabled("ServiceIO"))
            this.serviceController = new ServiceController(this);
    }

    private void initMotd() {
        var motd = config().general().motd();
        if (motd != null) getServer().motd(MiniMessage.miniMessage().deserialize(motd));
    }

    public ComponentBundle bundle() {
        return Objects.requireNonNull(bundle, "Bundle not initialized yet!");
    }

    public PluginConfig config() {
        return Objects.requireNonNull(config, "Config not initialized yet!").getRoot();
    }
}
