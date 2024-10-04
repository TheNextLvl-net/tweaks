package net.thenextlvl.tweaks.command.message;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class ReplyCommand {
    private final TweaksPlugin plugin;

    public void register(Commands commands) {
        var command = Commands.literal(plugin.commands().reply().command())
                .requires(stack -> stack.getSender().hasPermission("tweaks.command.msg.reply"))
                .then(Commands.argument("message", StringArgumentType.greedyString())
                        .executes(this::reply))
                .build();
        commands.register(command, "Reply to the last received private message", plugin.commands().reply().aliases());
    }

    private int reply(CommandContext<CommandSourceStack> context) {
        var sender = context.getSource().getSender();
        var target = plugin.msgController().getConversation(sender);

        if (target == null) {
            plugin.bundle().sendMessage(sender, "command.msg.conversation");
            return 0;
        }

        MSGCommand.message(plugin, context, sender, target);
        return Command.SINGLE_SUCCESS;
    }
}
