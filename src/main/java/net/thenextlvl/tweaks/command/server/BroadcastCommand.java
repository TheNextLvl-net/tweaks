package net.thenextlvl.tweaks.command.server;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class BroadcastCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal("broadcast")
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.broadcast"))
                .then(Commands.argument("message", StringArgumentType.greedyString())
                        .executes(this::broadcast))
                .build();
        registrar.register(command, "Broadcast a message", List.of("bc"));
    }

    private int broadcast(CommandContext<CommandSourceStack> context) {
        var message = context.getArgument("message", String.class)
                .replace("\\t", "   ");
        plugin.getServer().forEachAudience(audience -> {
            plugin.bundle().sendMessage(audience, "broadcast.header");
            var format = format(plugin.bundle().format(audience, "broadcast.format"), message);
            plugin.bundle().sendRawMessage(audience, format);
            plugin.bundle().sendMessage(audience, "broadcast.footer");
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
