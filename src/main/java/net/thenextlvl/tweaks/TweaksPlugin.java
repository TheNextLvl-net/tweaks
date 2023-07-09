package net.thenextlvl.tweaks;

import com.tcoded.folialib.FoliaLib;
import core.annotation.FieldsAreNonnullByDefault;
import core.api.file.format.GsonFile;
import core.api.placeholder.Placeholder;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.thenextlvl.tweaks.command.api.CommandBuilder;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.environment.*;
import net.thenextlvl.tweaks.command.item.*;
import net.thenextlvl.tweaks.command.player.*;
import net.thenextlvl.tweaks.command.server.BroadcastCommand;
import net.thenextlvl.tweaks.config.BackConfig;
import net.thenextlvl.tweaks.config.BroadcastConfig;
import net.thenextlvl.tweaks.config.TweaksConfig;
import net.thenextlvl.tweaks.util.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
@FieldsAreNonnullByDefault
public class TweaksPlugin extends JavaPlugin {
    @Accessors(fluent = true)
    private final Placeholder.Formatter<CommandSender> formatter = new Placeholder.Formatter<>();

    private final TweaksConfig tweaksConfig = new GsonFile<>(
            new File(getDataFolder(), "config.json"),
            new TweaksConfig(
                    new BroadcastConfig("", "<red>Server <grey>| <message>", ""),
                    new InventoryConfig(
                            new ConfigItem(Material.LIME_STAINED_GLASS_PANE, "§8» §aHelmet"),
                            new ConfigItem(Material.LIME_STAINED_GLASS_PANE, "§8» §aChestplate"),
                            new ConfigItem(Material.LIME_STAINED_GLASS_PANE, "§8» §aLeggings"),
                            new ConfigItem(Material.LIME_STAINED_GLASS_PANE, "§8» §aBoots"),
                            new ConfigItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "§8» §bOff Hand"),
                            new ConfigItem(Material.CYAN_STAINED_GLASS_PANE, "§8» §3Cursor"),
                            new ConfigItem(Material.IRON_BARS, "§7-§8/§7-")
                    )
            )
    ) {{
        if (!getFile().exists()) save();
    }}.getRoot();

    private final FoliaLib foliaLib = new FoliaLib(this);

    @Override
    public void onLoad() {
        Placeholders.init(this);
    }

    @Override
    public void onEnable() {
        registerCommands();
    }

    private void registerCommands() {
        registerCommand(new DayCommand(this));
        registerCommand(new NightCommand(this));
        registerCommand(new RainCommand(this));
        registerCommand(new SunCommand(this));
        registerCommand(new ThunderCommand(this));

        // Player
        registerCommand(new GodCommand());
        registerCommand(new FeedCommand());
        registerCommand(new FlyCommand());
        registerCommand(new HatCommand());
        registerCommand(new HealCommand());
        registerCommand(new PingCommand());
        registerCommand(new BackCommand(this));
        registerCommand(new SeenCommand(this));
        registerCommand(new InventoryCommand(this));
        registerCommand(new EnderChestCommand(this));
        registerCommand(new SpeedCommand());
        registerCommand(new GameModeCommand());

        // Server
        registerCommand(new BroadcastCommand(this));

        // Item
        registerCommand(new HeadCommand());
        registerCommand(new UnenchantCommand());
        registerCommand(new RepairCommand());
        registerCommand(new LoreCommand());
        registerCommand(new RenameCommand());
        registerCommand(new EnchantCommand());
        registerCommand(new ItemCommand());
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
}
