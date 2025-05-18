package net.thenextlvl.tweaks.command.warp;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.gui.WarpGUI;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class WarpsCommand {
    private final TweaksPlugin plugin;

    public WarpsCommand(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().warps.command)
                .requires(stack -> stack.getSender().hasPermission("tweaks.command.warp"))
                .executes(this::warps)
                .build();
        registrar.register(command, "List all available warps", plugin.commands().warps.aliases);
    }

    private int warps(CommandContext<CommandSourceStack> context) {
        var sender = context.getSource().getSender();
        plugin.warpController().getWarps().thenAccept(warps -> {
            if (warps.isEmpty()) {
                plugin.bundle().sendMessage(sender, "command.warp.empty");
            } else if (plugin.config().guis.warps.enabled && sender instanceof Player player) {
                player.getScheduler().run(plugin, task -> new WarpGUI(plugin, player, warps).open(), null);
            } else {
                var list = warps.stream().map(warp -> {
                    var event = ClickEvent.runCommand("/warp " + warp.getName());
                    return Component.text(warp.getName())
                            .hoverEvent(HoverEvent.showText(plugin.bundle().component("chat.click.teleport", sender)))
                            .clickEvent(event);
                }).toList();
                plugin.bundle().sendMessage(sender, "command.warp.list",
                        Formatter.number("amount", warps.size()),
                        Formatter.joining("warps", list));
            }
        }).exceptionally(throwable -> {
            plugin.getComponentLogger().error("Failed to retrieve warps", throwable);
            return null;
        });
        return Command.SINGLE_SUCCESS;
    }
}
