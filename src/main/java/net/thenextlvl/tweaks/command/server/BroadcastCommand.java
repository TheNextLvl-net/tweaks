package net.thenextlvl.tweaks.command.server;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class BroadcastCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().broadcast().command())
                .requires(stack -> stack.getSender().hasPermission("tweaks.command.broadcast"))
                .then(Commands.argument("message", StringArgumentType.greedyString())
                        .executes(this::broadcast))
                .build();
        registrar.register(command, "Broadcast a message", plugin.commands().broadcast().aliases());
    }

    private int broadcast(CommandContext<CommandSourceStack> context) {
        var message = context.getArgument("message", String.class)
                .replace("\\t", "   ");
        plugin.getServer().forEachAudience(audience -> {
            plugin.bundle().sendMessage(audience, "command.broadcast.header");
            var format = format(plugin.bundle().format(audience, "command.broadcast.format"), message);
            plugin.bundle().sendRawMessage(audience, format);
            plugin.bundle().sendMessage(audience, "command.broadcast.footer");
        });
        return Command.SINGLE_SUCCESS;
    }

    private String format(@Nullable String format, String message) {
        if (format == null) return message.replace("\\\\n", "\n");
        var split = message.split("(\\\\n|<br>|<newline>)");
        for (int i = 0; i < split.length; i++) split[i] = format.replace("<message>", split[i]);
        return String.join("\n", split);
    }
}
