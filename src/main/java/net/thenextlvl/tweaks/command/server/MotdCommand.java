package net.thenextlvl.tweaks.command.server;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@CommandInfo(
        name = "motd",
        permission = "tweaks.command.motd",
        description = "change the motd of the server",
        usage = "/motd [message]"
)
@RequiredArgsConstructor
public class MotdCommand implements CommandExecutor {
    private final TweaksPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        var message = String.join(" ", args);
        var motd = MiniMessage.miniMessage().deserialize(message);
        plugin.bundle().sendMessage(sender, "motd.changed", Placeholder.component("motd", motd));
        plugin.config().serverConfig().motd(message);
        plugin.configFile().save();
        Bukkit.motd(motd);
        return true;
    }
}
