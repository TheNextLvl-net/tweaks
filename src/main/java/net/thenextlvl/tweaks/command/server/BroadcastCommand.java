package net.thenextlvl.tweaks.command.server;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CommandInfo(
        name = "broadcast",
        permission = "tweaks.command.broadcast",
        description = "broadcast a message",
        usage = "/<command> [message]",
        aliases = {"bc"}
)
@RequiredArgsConstructor
public class BroadcastCommand implements TabExecutor {
    private final TweaksPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) return false;

        var message = String.join(" ", args).replace("\\t", "   ");

        var receivers = new ArrayList<Audience>(Bukkit.getOnlinePlayers());
        receivers.add(Bukkit.getConsoleSender());

        receivers.forEach(audience -> {
            plugin.bundle().sendMessage(audience, "broadcast.header");
            var format = format(plugin.bundle().format(audience, "broadcast.format"), message);
            plugin.bundle().sendRawMessage(audience, format);
            plugin.bundle().sendMessage(audience, "broadcast.footer");
        });

        return true;
    }

    private String format(@Nullable String format, String message) {
        if (format == null) return message.replace("\\\\n", "\n");
        var split = message.split("\\\\n");
        for (int i = 0; i < split.length; i++) split[i] = format.replace("<message>", split[i]);
        return String.join("\n", split);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Arrays.asList(args[args.length - 1] + "\\n", args[args.length - 1] + "\\t");
    }
}
