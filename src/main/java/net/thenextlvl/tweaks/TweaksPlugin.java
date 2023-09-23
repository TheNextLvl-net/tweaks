package net.thenextlvl.tweaks;

import core.annotation.FieldsAreNotNullByDefault;
import core.api.file.format.GsonFile;
import core.i18n.file.ComponentBundle;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.thenextlvl.tweaks.command.api.CommandBuilder;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.environment.*;
import net.thenextlvl.tweaks.command.item.*;
import net.thenextlvl.tweaks.command.player.*;
import net.thenextlvl.tweaks.command.server.BroadcastCommand;
import net.thenextlvl.tweaks.command.workstation.*;
import net.thenextlvl.tweaks.config.*;
import net.thenextlvl.tweaks.listener.ChatListener;
import net.thenextlvl.tweaks.listener.ConnectionListener;
import net.thenextlvl.tweaks.listener.EntityListener;
import net.thenextlvl.tweaks.listener.WorldListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Locale;

@Getter
@Accessors(fluent = true)
@FieldsAreNotNullByDefault
public class TweaksPlugin extends JavaPlugin {
    private final Metrics metrics = new Metrics(this, 19651);

    private final TweaksConfig config = new GsonFile<>(
            new File(getDataFolder(), "config.json"),
            new TweaksConfig(
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
                    new VanillaTweaks(0, 0, 0, false)
            )
    ) {{
        if (getRoot().generalConfig() == null)
            getLogger().severe("Your general-config-section is malformed");
        if (getRoot().inventoryConfig() == null)
            getLogger().severe("Your inventory-config-section is malformed");
        if (getRoot().vanillaTweaks() == null)
            getLogger().severe("Your vanilla-tweaks-section is malformed");
        if (!getFile().exists()) save();
    }}.getRoot();
    private final File translations = new File(getDataFolder(), "translations");
    private final ComponentBundle bundle = new ComponentBundle(translations, audience ->
            audience instanceof Player player ? player.locale() : Locale.US)
            .register("tweaks", Locale.US)
            .register("tweaks_german", Locale.GERMANY)
            .fallback(Locale.US);

    @Override
    public void onLoad() {
        bundle().miniMessage(MiniMessage.builder()
                .tags(TagResolver.builder()
                        .resolvers(TagResolver.standard())
                        .resolver(Placeholder.component("prefix", bundle.component(Locale.US, "prefix")))
                        .build())
                .build());
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
        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ConnectionListener(this), this);
        Bukkit.getPluginManager().registerEvents(new EntityListener(this), this);
        Bukkit.getPluginManager().registerEvents(new WorldListener(this), this);
    }

    private void registerCommands() {
        registerCommand(new DayCommand(this));
        registerCommand(new NightCommand(this));
        registerCommand(new RainCommand(this));
        registerCommand(new SunCommand(this));
        registerCommand(new ThunderCommand(this));

        // Player
        registerCommand(new GodCommand(this));
        registerCommand(new FeedCommand(this));
        registerCommand(new FlyCommand(this));
        registerCommand(new HatCommand(this));
        registerCommand(new HealCommand(this));
        registerCommand(new PingCommand(this));
        registerCommand(new BackCommand(this));
        registerCommand(new SeenCommand(this));
        registerCommand(new InventoryCommand(this));
        registerCommand(new EnderChestCommand(this));
        registerCommand(new SpeedCommand(this));
        registerCommand(new GameModeCommand(this));

        // Server
        registerCommand(new BroadcastCommand(this));

        // Item
        registerCommand(new HeadCommand(this));
        registerCommand(new UnenchantCommand(this));
        registerCommand(new RepairCommand(this));
        registerCommand(new LoreCommand(this));
        registerCommand(new RenameCommand(this));
        registerCommand(new EnchantCommand(this));
        registerCommand(new ItemCommand(this));

        // Workstation
        registerCommand(new AnvilCommand());
        registerCommand(new CartographyTableCommand());
        registerCommand(new EnchantingTableCommand());
        registerCommand(new GrindstoneCommand());
        registerCommand(new LoomCommand());
        registerCommand(new SmithingTableCommand());
        registerCommand(new StonecutterCommand());
        registerCommand(new WorkbenchCommand());
    }

    private void registerCommand(CommandExecutor executor) {
        try {
            var annotation = executor.getClass().getAnnotation(CommandInfo.class);
            if (annotation == null) throw new IllegalStateException("CommandInfo not defined");
            var tabCompleter = executor instanceof TabCompleter completer ? completer : null;
            var builder = new CommandBuilder(this, annotation, executor, tabCompleter);
            Bukkit.getCommandMap().register(getName(), builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TweaksPlugin get() {
        return JavaPlugin.getPlugin(TweaksPlugin.class);
    }
}
