package net.nonswag.tnl.tweaks.completer;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class OPCommandTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (sender.hasPermission("tnl.rights") && args.length <= 1) {
            for (OfflinePlayer all : Bukkit.getOfflinePlayers()) if (!all.isOp()) suggestions.add(all.getName());
        }
        suggestions.removeIf(tabCompleter -> !tabCompleter.toLowerCase().startsWith(args[args.length - 1].toLowerCase()));
        return suggestions;
    }
}
