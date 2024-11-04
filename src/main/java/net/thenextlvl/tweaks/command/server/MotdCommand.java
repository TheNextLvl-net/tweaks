package net.thenextlvl.tweaks.command.server;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class MotdCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().motd().command())
                .requires(stack -> stack.getSender().hasPermission("tweaks.command.motd"))
                .then(Commands.literal("replace")
                        .then(Commands.argument("text", StringArgumentType.string())
                                .then(Commands.argument("replacement", StringArgumentType.greedyString())
                                        .executes(this::replace))))
                .then(Commands.literal("set")
                        .then(Commands.argument("motd", StringArgumentType.greedyString())
                                .executes(this::set)))
                .then(Commands.literal("clear")
                        .executes(this::clear))
                .then(Commands.literal("get")
                        .executes(this::get))
                .build();
        registrar.register(command, "Change the motd of the server", plugin.commands().motd().aliases());
    }

    private int replace(CommandContext<CommandSourceStack> context) {
        var sender = context.getSource().getSender();
        var motd = plugin.config().general().motd();
        if (motd == null) {
            plugin.bundle().sendMessage(sender, "command.motd.none");
            return 0;
        }
        var message = context.getArgument("text", String.class);
        var replacement = context.getArgument("replacement", String.class);
        var newMotd = motd.replace(message, replacement);
        return updateMotd(sender, newMotd);
    }

    private int clear(CommandContext<CommandSourceStack> context) {
        var sender = context.getSource().getSender();
        return updateMotd(sender, null);
    }

    private int get(CommandContext<CommandSourceStack> context) {
        var sender = context.getSource().getSender();
        var motd = plugin.config().general().motd();
        var message = motd != null ? "command.motd" : "command.motd.none";
        plugin.bundle().sendMessage(sender, message, Placeholder.parsed("motd", String.valueOf(motd)));
        return Command.SINGLE_SUCCESS;
    }

    private int set(CommandContext<CommandSourceStack> context) {
        var sender = context.getSource().getSender();
        var motd = context.getArgument("motd", String.class);
        return updateMotd(sender, motd);
    }

    private int updateMotd(CommandSender sender, @Nullable String motd) {
        var message = motd == null ? Component.text("A Minecraft Server")
                : MiniMessage.miniMessage().deserialize(motd);
        plugin.bundle().sendMessage(sender, "command.motd.changed",
                Placeholder.component("motd", message));
        plugin.config().general().motd(motd);
        plugin.saveConfig();
        plugin.getServer().motd(message);
        return Command.SINGLE_SUCCESS;
    }
}
