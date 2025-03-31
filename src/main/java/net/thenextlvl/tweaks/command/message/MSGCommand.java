package net.thenextlvl.tweaks.command.message;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import core.paper.command.CustomArgumentTypes;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.suggestion.MSGSuggestionProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
@RequiredArgsConstructor
public class MSGCommand {
    private final TweaksPlugin plugin;

    public void register(Commands commands) {
        var command = Commands.literal(plugin.commands().msg().command())
                .requires(stack -> stack.getSender().hasPermission("tweaks.command.msg"))
                .then(Commands.argument("player", CustomArgumentTypes.playerExact())
                        .suggests(new MSGSuggestionProvider(plugin))
                        .then(Commands.argument("message", StringArgumentType.greedyString())
                                .executes(this::message)))
                .build();
        commands.register(command, "Send a private message to a player", plugin.commands().msg().aliases());
    }

    private int message(CommandContext<CommandSourceStack> context) {
        var sender = context.getSource().getSender();
        var target = context.getArgument("player", Player.class);

        if (sender.equals(target)) {
            plugin.bundle().sendMessage(sender, "command.msg.self");
            return 0;
        }

        if (!plugin.msgController().isConversing(sender, target)
            && plugin.dataController().isMsgToggled(target)) {
            plugin.bundle().sendMessage(sender, "command.msg.toggled",
                    Placeholder.parsed("player", target.getName()));
            return 0;
        }

        message(plugin, context, sender, target);
        return Command.SINGLE_SUCCESS;
    }

    static void message(TweaksPlugin plugin, CommandContext<CommandSourceStack> context, CommandSender sender, CommandSender target) {
        var message = context.getArgument("message", String.class);

        plugin.bundle().sendMessage(sender, "command.msg.outgoing",
                Placeholder.parsed("receiver", target.getName()),
                Placeholder.parsed("sender", sender.getName()),
                Placeholder.parsed("message", message));
        plugin.bundle().sendMessage(target, "command.msg.incoming",
                Placeholder.parsed("receiver", target.getName()),
                Placeholder.parsed("sender", sender.getName()),
                Placeholder.parsed("message", message));

        plugin.msgController().startConversation(target, sender);
    }
}
