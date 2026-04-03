package net.thenextlvl.tweaks.command.message;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class MSGToggleCommand {
    private final TweaksPlugin plugin;

    public MSGToggleCommand(final TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(final Commands commands) {
        final var command = Commands.literal(plugin.commands().msgToggle.command)
                .requires(stack -> stack.getSender() instanceof final Player player
                                   && player.hasPermission("tweaks.command.msg.toggle"))
                .executes(this::toggle)
                .build();
        commands.register(command, "Toggle receiving private messages", plugin.commands().msgToggle.aliases);
    }

    private int toggle(final CommandContext<CommandSourceStack> context) {
        final var sender = (Player) context.getSource().getSender();
        final var disabled = plugin.dataController().toggleMsg(sender);
        final var message = disabled ? "command.msg.disabled" : "command.msg.enabled";
        plugin.bundle().sendMessage(sender, message);
        plugin.msgController().removeExternalConversations(sender);
        return Command.SINGLE_SUCCESS;
    }
}
