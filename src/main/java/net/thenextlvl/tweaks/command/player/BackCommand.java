package net.thenextlvl.tweaks.command.player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.GameRules;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.COMMAND;

@NullMarked
public class BackCommand {
    private final TweaksPlugin plugin;

    public BackCommand(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().back.command)
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.back"))
                .executes(this::back)
                .build();
        registrar.register(command, "Go back to your last position", plugin.commands().back.aliases);
    }

    private int back(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();

        var location = plugin.backController().peekFirst(player);

        if (location == null) {
            plugin.bundle().sendMessage(player, "command.back.none");
            return 0;
        }

        plugin.backController().lock(player, location);

        plugin.teleportController().teleport(player, location, COMMAND).thenAccept(success -> {
            var message = success ? "command.back" : "command.teleport.cancelled";
            if (success) plugin.backController().remove(player, location);
            if (Boolean.TRUE.equals(player.getWorld().getGameRuleValue(GameRules.SEND_COMMAND_FEEDBACK)) || !success)
                plugin.bundle().sendMessage(player, message);
        });
        return Command.SINGLE_SUCCESS;
    }
}
