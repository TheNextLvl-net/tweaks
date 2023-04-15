package net.thenextlvl.tweaks.command.api;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public record CommandBuilder(Plugin plugin, CommandInfo info,
                             CommandExecutor executor, @Nullable TabCompleter tabCompleter) {

    public PluginCommand build() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
        constructor.trySetAccessible();
        var command = constructor.newInstance(info().name(), plugin());
        command.setAliases(Arrays.asList(info().aliases()));
        command.setDescription(info().description());
        command.setPermission(info().permission());
        command.setUsage(info().usage());
        command.setTabCompleter(tabCompleter());
        command.setExecutor((sender, command1, label, args) -> {
            try {
                return executor().onCommand(sender, command1, label, args);
            } catch (CommandException e) {
                e.handleException(sender);
                return true;
            }
        });
        return command;
    }
}
