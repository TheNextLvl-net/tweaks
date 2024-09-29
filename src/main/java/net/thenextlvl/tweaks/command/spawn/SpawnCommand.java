package net.thenextlvl.tweaks.command.spawn;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;

import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.COMMAND;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class SpawnCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().spawn().command())
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("extra-tweaks.spawn"))
                .executes(context -> {
                    var player = (Player) context.getSource().getSender();
                    var location = plugin.config().spawn().location();
                    if (location == null) {
                        plugin.bundle().sendMessage(player, "command.spawn.undefined");
                        if (player.hasPermission("extra-tweaks.setspawn"))
                            plugin.bundle().sendMessage(player, "command.spawn.define");
                    } else plugin.teleportController().teleport(player, location, COMMAND).thenAccept(success -> {
                        var message = success ? "command.spawn" : "command.teleport.cancelled";
                        plugin.bundle().sendMessage(player, message);
                    });
                    return location != null ? Command.SINGLE_SUCCESS : 0;
                }).build();
        registrar.register(command, "Teleport you to spawn", plugin.commands().spawn().aliases());
    }
}