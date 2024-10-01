package net.thenextlvl.tweaks.command.server;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class MotdCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().motd().command())
                .requires(stack -> stack.getSender().hasPermission("tweaks.command.motd"))
                .then(Commands.argument("motd", StringArgumentType.greedyString()))
                .executes(context -> {
                    var sender = context.getSource().getSender();
                    var message = context.getArgument("motd", String.class);
                    var motd = MiniMessage.miniMessage().deserialize(message);
                    plugin.bundle().sendMessage(sender, "command.motd.changed", Placeholder.component("motd", motd));
                    plugin.config().general().motd(message);
                    plugin.saveConfig();
                    plugin.getServer().motd(motd);
                    return Command.SINGLE_SUCCESS;
                })
                .build();
        registrar.register(command, "Change the motd of the server", plugin.commands().motd().aliases());
    }
}
