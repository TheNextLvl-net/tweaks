package net.thenextlvl.tweaks.command.server;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;

@NullMarked
public class BroadcastCommand {
    private final TweaksPlugin plugin;

    public BroadcastCommand(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().broadcast.command)
                .requires(stack -> stack.getSender().hasPermission("tweaks.command.broadcast"))
                .then(Commands.argument("message", StringArgumentType.greedyString())
                        .executes(this::broadcast))
                .build();
        registrar.register(command, "Broadcast a message", plugin.commands().broadcast.aliases);
    }

    private int broadcast(CommandContext<CommandSourceStack> context) {
        var lines = Arrays.asList(context.getArgument("message", String.class)
                .replace("\\t", "   ").replace("\\\\n", "\n")
                .split("(\\\\n|<br>|<newline>)"));
        plugin.getServer().forEachAudience(audience -> {
            plugin.bundle().sendMessage(audience, "command.broadcast.header");
            lines.forEach(line -> plugin.bundle().sendMessage(audience, "command.broadcast.format",
                    Placeholder.parsed("message", line)));
            plugin.bundle().sendMessage(audience, "command.broadcast.footer");
        });
        return Command.SINGLE_SUCCESS;
    }
}
