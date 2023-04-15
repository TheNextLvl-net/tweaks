package net.thenextlvl.tweaks;

import core.annotation.FieldsAreNonnullByDefault;
import core.api.placeholder.Placeholder;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.thenextlvl.tweaks.command.api.CommandBuilder;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.environment.*;
import net.thenextlvl.tweaks.command.item.RepairCommand;
import net.thenextlvl.tweaks.command.item.UnenchantCommand;
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

@Getter
@FieldsAreNonnullByDefault
public class TweaksPlugin extends JavaPlugin {
    @Accessors(fluent = true)
    private final Placeholder.Formatter<CommandSender> formatter = new Placeholder.Formatter<>();

    private final TweaksConfig tweaksConfig = new TweaksConfig(
            new BroadcastConfig("", "<red>Server <grey>| <message>", ""),
            new BackConfig(5)
    );

    @Override
    public void onLoad() {
        Placeholders.init(this);
    }

    @Override
    public void onEnable() {
        registerCommands();
    }

    private void registerCommands() {
        registerCommand(new DayCommand());
        registerCommand(new NightCommand());
        registerCommand(new RainCommand());
        registerCommand(new SunCommand());
        registerCommand(new ThunderCommand());

        // Player
        registerCommand(new FeedCommand());
        registerCommand(new FlyCommand());
        registerCommand(new HatCommand());
        registerCommand(new HealCommand());
        registerCommand(new PingCommand());
        registerCommand(new BackCommand(this));
        registerCommand(new SeenCommand(this));
        registerCommand(new InventoryCommand());
        registerCommand(new EnderChestCommand());

        // Server
        registerCommand(new BroadcastCommand(this));

        // Item
        registerCommand(new UnenchantCommand());
        registerCommand(new RepairCommand());
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
