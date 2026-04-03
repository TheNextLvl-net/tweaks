package net.thenextlvl.tweaks.command.tpa;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class TPAToggleCommand {
    private final TweaksPlugin plugin;

    public TPAToggleCommand(final TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(final Commands commands) {
        final var command = Commands.literal(plugin.commands().teleportToggle.command)
                .requires(stack -> stack.getSender() instanceof final Player player
                                   && player.hasPermission("tweaks.command.tpa.toggle"))
                .executes(this::toggle)
                .build();
        commands.register(command, "Toggle the ability to receive teleport requests", plugin.commands().teleportToggle.aliases);
    }

    private int toggle(final CommandContext<CommandSourceStack> context) {
        final var sender = (Player) context.getSource().getSender();
        final var disabled = plugin.dataController().toggleTpa(sender);
        final var message = disabled ? "command.tpa.disabled" : "command.tpa.enabled";
        plugin.bundle().sendMessage(sender, message);
        return Command.SINGLE_SUCCESS;
    }
}
