package net.thenextlvl.tweaks.command.api;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        command.setTabCompleter((sender, command1, label, args) ->
                strip(tabCompleter() != null ? tabCompleter().onTabComplete(sender, command1, label, args) : null, args));
        command.setExecutor((sender, command1, label, args) -> {
            execute(sender, command1, label, args);
            return true;
        });
        return command;
    }

    private List<String> strip(@Nullable List<String> suggestions, String[] args) {
        if (suggestions == null) return Collections.emptyList();
        if (suggestions.isEmpty()) return suggestions;
        return suggestions.stream().filter(s -> s.contains(args[args.length - 1])).toList();
    }

    private void execute(CommandSender sender, Command command, String label, String[] args) {
        try {
            if (executor().onCommand(sender, command, label, args)) return;
            sender.sendRichMessage("<lang:tweaks.command.usage>", Placeholder.parsed("usage", command.getUsage()
                    .replace("[", "<dark_gray>[<gold>").replace("]", "<dark_gray>]<red>")
                    .replace("(", "<dark_gray>(<gold>").replace(")", "<dark_gray>)<red>")
                    .replace("|", " <dark_gray>| <gold>").replace("<command>", label)));
        } catch (CommandException e) {
            e.handle(sender);
        }
    }
}
