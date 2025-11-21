package net.thenextlvl.tweaks.command.server;

import com.google.common.io.ByteStreams;
import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class LobbyCommand {
    private final TweaksPlugin plugin;

    public LobbyCommand(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().lobby.command)
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.lobby"))
                .executes(context -> {
                    var sender = (Player) context.getSource().getSender();
                    connect(sender, plugin.config().general.lobbyServerName);
                    return Command.SINGLE_SUCCESS;
                })
                .build();
        registrar.register(command, "Connect to the lobby server", plugin.commands().lobby.aliases);
    }
    
    public void connect(Player player, String server) {
        var dataOutput = ByteStreams.newDataOutput();
        dataOutput.writeUTF("Connect");
        dataOutput.writeUTF(server);
        player.sendPluginMessage(plugin, "BungeeCord", dataOutput.toByteArray());
    }
}
