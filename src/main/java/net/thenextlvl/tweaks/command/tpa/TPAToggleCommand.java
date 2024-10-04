package net.thenextlvl.tweaks.command.tpa;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class TPAToggleCommand {
    private final TweaksPlugin plugin;

    public void register(Commands commands) {
        var command = Commands.literal(plugin.commands().teleportToggle().command())
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.tpa.toggle"))
                .executes(this::toggle)
                .build();
        commands.register(command, "Toggle the ability to receive teleport requests", plugin.commands().teleportToggle().aliases());
    }

    private int toggle(CommandContext<CommandSourceStack> context) {
        var sender = (Player) context.getSource().getSender();
        var disabled = plugin.dataController().toggleTpa(sender);
        var message = disabled ? "command.tpa.disabled" : "command.tpa.enabled";
        plugin.bundle().sendMessage(sender, message);
        return Command.SINGLE_SUCCESS;
    }
}
