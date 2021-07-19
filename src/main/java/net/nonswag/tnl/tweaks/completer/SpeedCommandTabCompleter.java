package net.nonswag.tnl.tweaks.completer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class SpeedCommandTabCompleter implements TabCompleter {

    @Nonnull
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();
        for (int i = 0; i <= 10; i++) suggestions.add(String.valueOf(i));
        suggestions.removeIf(tabCompleter -> !tabCompleter.toLowerCase().startsWith(args[args.length - 1].toLowerCase()));
        return suggestions;
    }
}
