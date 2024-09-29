package net.thenextlvl.tweaks;

import core.file.FileIO;
import core.file.format.GsonFile;
import core.i18n.file.ComponentBundle;
import core.io.IO;
import core.paper.messenger.PluginMessenger;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.thenextlvl.tweaks.command.environment.time.DayCommand;
import net.thenextlvl.tweaks.command.environment.time.MidnightCommand;
import net.thenextlvl.tweaks.command.environment.time.NightCommand;
import net.thenextlvl.tweaks.command.environment.time.NoonCommand;
import net.thenextlvl.tweaks.command.environment.weather.RainCommand;
import net.thenextlvl.tweaks.command.environment.weather.SunCommand;
import net.thenextlvl.tweaks.command.environment.weather.ThunderCommand;
import net.thenextlvl.tweaks.command.item.*;
import net.thenextlvl.tweaks.command.player.*;
import net.thenextlvl.tweaks.command.server.BroadcastCommand;
import net.thenextlvl.tweaks.command.server.LobbyCommand;
import net.thenextlvl.tweaks.command.server.MotdCommand;
import net.thenextlvl.tweaks.command.workstation.*;
import net.thenextlvl.tweaks.config.*;
import net.thenextlvl.tweaks.listener.ChatListener;
import net.thenextlvl.tweaks.listener.ConnectionListener;
import net.thenextlvl.tweaks.listener.EntityListener;
import net.thenextlvl.tweaks.listener.WorldListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Locale;

@Getter
@Accessors(fluent = true)
@SuppressWarnings("UnstableApiUsage")
public class TweaksPlugin extends JavaPlugin {
    private final Metrics metrics = new Metrics(this, 19651);
    private final PluginMessenger messenger = new PluginMessenger(this);

    private final FileIO<TweaksConfig> configFile = new GsonFile<>(
            IO.of(getDataFolder(), "config.json"), new TweaksConfig(
            new GeneralConfig(5, (byte) -1, false, false, false, true),
            new InventoryConfig(
                    new ConfigItem(Material.LIME_STAINED_GLASS_PANE, "§8» §aHelmet"),
                    new ConfigItem(Material.LIME_STAINED_GLASS_PANE, "§8» §aChestplate"),
                    new ConfigItem(Material.LIME_STAINED_GLASS_PANE, "§8» §aLeggings"),
                    new ConfigItem(Material.LIME_STAINED_GLASS_PANE, "§8» §aBoots"),
                    new ConfigItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "§8» §bOff Hand"),
                    new ConfigItem(Material.CYAN_STAINED_GLASS_PANE, "§8» §3Cursor"),
                    new ConfigItem(Material.IRON_BARS, "§7-§8/§7-"),
                    20
            ),
            new VanillaTweaks(0, 0, 0, false),
            new ServerConfig(isProxyEnabled(), "lobby", null)
    )).validate().save();

    private boolean isProxyEnabled() {
        return getServer().spigot().getPaperConfig().getBoolean("proxies.velocity.enabled")
               || getServer().spigot().getSpigotConfig().getBoolean("settings.bungeecord");
    }

    private final File translations = new File(getDataFolder(), "translations");
    private final ComponentBundle bundle = new ComponentBundle(translations, audience ->
            audience instanceof Player player ? player.locale() : Locale.US)
            .register("tweaks", Locale.US)
            .register("tweaks_german", Locale.GERMANY)
            .miniMessage(bundle -> MiniMessage.builder().tags(TagResolver.resolver(
                    TagResolver.standard(),
                    Placeholder.component("prefix", bundle.component(Locale.US, "prefix"))
            )).build());

    @Override
    public void onLoad() {
        var motd = config().serverConfig().motd();
        if (motd != null) getServer().motd(MiniMessage.miniMessage().deserialize(motd));
    }

    @Override
    public void onEnable() {
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        metrics.shutdown();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new ConnectionListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityListener(this), this);
        getServer().getPluginManager().registerEvents(new WorldListener(this), this);
    }

    private void registerCommands() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(event -> {
            var registrar = event.registrar();
            registerItemCommands(registrar);
            registerPlayerCommands(registrar);
            registerServerCommands(registrar);
            registerTimeCommands(registrar);
            registerWeatherCommands(registrar);
            registerWorkstationCommands(registrar);
        }));
    }

    private void registerTimeCommands(Commands registrar) {
        new DayCommand(this).register(registrar);
        new MidnightCommand(this).register(registrar);
        new NightCommand(this).register(registrar);
        new NoonCommand(this).register(registrar);
    }

    private void registerWeatherCommands(Commands registrar) {
        new RainCommand(this).register(registrar);
        new SunCommand(this).register(registrar);
        new ThunderCommand(this).register(registrar);
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
        new VanishCommand(this).register(registrar);
    }

    private void registerServerCommands(Commands registrar) {
        new BroadcastCommand(this).register(registrar);
        if (config().serverConfig().enableLobbyCommand())
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

    public TweaksConfig config() {
        return configFile().getRoot();
    }
}
