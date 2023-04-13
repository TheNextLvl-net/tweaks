package net.thenextlvl.tweaks;

import lombok.Getter;
import net.thenextlvl.tweaks.command.api.CommandBuilder;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.environment.*;
import net.thenextlvl.tweaks.command.server.BroadcastCommand;
import net.thenextlvl.tweaks.config.BroadcastConfig;
import net.thenextlvl.tweaks.config.TweaksConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class TweaksPlugin extends JavaPlugin {

    private final TweaksConfig tweaksConfig = new TweaksConfig(
            new BroadcastConfig("<red>Server <grey>| <message>")
    );

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
        registerCommand(new BroadcastCommand(this));

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
