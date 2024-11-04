package net.thenextlvl.tweaks.command.warp;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.gui.WarpGUI;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class WarpsCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().warps().command())
                .requires(stack -> stack.getSender().hasPermission("tweaks.command.warp"))
                .executes(this::warps)
                .build();
        registrar.register(command, "List all available warps", plugin.commands().warps().aliases());
    }

    private int warps(CommandContext<CommandSourceStack> context) {
        var sender = context.getSource().getSender();
        plugin.warpController().getWarps().thenAccept(warps -> {
            if (warps.isEmpty()) {
                plugin.bundle().sendMessage(sender, "command.warp.empty");
            } else if (plugin.config().guis().warps().enabled() && sender instanceof Player player) {
                plugin.getServer().getScheduler().runTask(plugin, () -> new WarpGUI(plugin, player, warps).open());
            } else {
                var list = Component.join(JoinConfiguration.commas(true), warps.stream().map(warp -> {
                    var event = ClickEvent.runCommand("/warp " + warp.getName());
                    return Component.text(warp.getName())
                            .hoverEvent(HoverEvent.showText(plugin.bundle().component(sender, "chat.click.teleport")))
                            .clickEvent(event);
                }).toList());
                plugin.bundle().sendMessage(sender, "command.warp.list",
                        Placeholder.parsed("amount", String.valueOf(warps.size())),
                        Placeholder.component("warps", list));
            }
        }).exceptionally(throwable -> {
            plugin.getComponentLogger().error("Failed to retrieve warps", throwable);
            return null;
        });
        return Command.SINGLE_SUCCESS;
    }
}
