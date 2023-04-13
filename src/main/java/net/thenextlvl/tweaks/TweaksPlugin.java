package net.thenextlvl.tweaks;

import lombok.Getter;
import net.thenextlvl.tweaks.api.command.CommandInfo;
import net.thenextlvl.tweaks.command.environment.*;
import net.thenextlvl.tweaks.command.server.BroadcastCommand;
import net.thenextlvl.tweaks.config.BroadcastConfig;
import net.thenextlvl.tweaks.config.TweaksConfig;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

@Getter
public class TweaksPlugin extends JavaPlugin {

    private TweaksConfig tweaksConfig = new TweaksConfig(
            new BroadcastConfig("<red>Server <grey>| <message>")
    );

    @Override
    public void onDisable() {

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
        registerCommand(new BroadcastCommand(this));

    }

    private void registerCommand(CommandExecutor executor) {
        CommandInfo annotation = executor.getClass().getAnnotation(CommandInfo.class);
        if (annotation == null)
            return;

        CommandMap commandMap = getServer().getCommandMap();
        try {
            Constructor<PluginCommand> declaredConstructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            declaredConstructor.trySetAccessible();
            PluginCommand pluginCommand = declaredConstructor.newInstance(annotation.name(), this);
            pluginCommand.setExecutor(executor);
            if (executor instanceof TabCompleter completer) {
                pluginCommand.setTabCompleter(completer);
            }
            pluginCommand.setAliases(Arrays.asList(annotation.aliases()));
            pluginCommand.setDescription("teeeee"); // TODO: Translate

            if (!annotation.permission().isBlank())
                pluginCommand.setPermission(annotation.permission());
            if (!annotation.usage().isBlank())
                pluginCommand.setUsage(annotation.usage());

            commandMap.register(getDescription().getName(), pluginCommand);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
