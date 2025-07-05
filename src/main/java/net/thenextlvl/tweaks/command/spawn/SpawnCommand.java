package net.thenextlvl.tweaks.command.spawn;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.COMMAND;

@NullMarked
public class SpawnCommand {
    private final TweaksPlugin plugin;

    public SpawnCommand(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().spawn.command)
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.spawn"))
                .executes(this::spawn).build();
        registrar.register(command, "Teleport you to spawn", plugin.commands().spawn.aliases);
    }

    private int spawn(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        var location = plugin.config().spawn.location;
        if (location == null || !location.isWorldLoaded()) {
            plugin.bundle().sendMessage(player, "command.spawn.undefined");
            if (player.hasPermission("tweaks.command.setspawn"))
                plugin.bundle().sendMessage(player, "command.spawn.define");
            return 0;
        } else plugin.teleportController().teleport(player, location, COMMAND).thenAccept(success -> {
            var message = success ? "command.spawn" : "command.teleport.cancelled";
            if (Boolean.TRUE.equals(player.getWorld().getGameRuleValue(GameRule.SEND_COMMAND_FEEDBACK)) || !success)
                plugin.bundle().sendMessage(player, message);
        });
        return Command.SINGLE_SUCCESS;
    }
}
