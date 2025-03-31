package net.thenextlvl.tweaks.command.player;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PingCommand extends PlayerCommand {
    public PingCommand(TweaksPlugin plugin) {
        super(plugin);
    }

    public void register(Commands registrar) {
        var command = create(plugin.commands().ping.command, "tweaks.command.ping", "tweaks.command.ping.others");
        registrar.register(command, "See your own or someone else's latency", plugin.commands().ping.aliases);
    }

    @Override
    protected int execute(CommandSender sender, Player player) {
        if (sender != player) plugin.bundle().sendMessage(sender, "command.ping.others",
                Placeholder.parsed("player", player.getName()),
                Placeholder.parsed("ping", String.valueOf(player.getPing())));
        else plugin.bundle().sendMessage(player, "command.ping.self",
                Placeholder.parsed("ping", String.valueOf(player.getPing())));
        return Command.SINGLE_SUCCESS;
    }
}
