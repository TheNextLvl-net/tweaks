package net.thenextlvl.tweaks.command.api;

import core.api.placeholder.Placeholder;
import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.command.*;
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
            execute(sender, command1, label, args);
            return true;
        });
        return command;
    }

    private void execute(CommandSender sender, Command command1, String label, String[] args) {
        try {
            if (executor().onCommand(sender, command1, label, args)) return;
            var usage = Placeholder.<CommandSender>of("usage", command1.getUsage()
                    .replace("[", "§8[§6").replace("]", "§8]§c")
                    .replace("(", "§8(§6").replace(")", "§8)§c")
                    .replace("<command>", label));
            sender.sendPlainMessage(Messages.COMMAND_USAGE.message(sender, usage));
        } catch (CommandException e) {
            e.handleException(sender);
        }
    }
}
