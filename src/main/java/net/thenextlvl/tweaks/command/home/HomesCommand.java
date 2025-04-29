package net.thenextlvl.tweaks.command.home;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.gui.HomeGUI;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class HomesCommand {
    private final TweaksPlugin plugin;

    public HomesCommand(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().homes.command)
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.home"))
                .executes(this::homes)
                .build();
        registrar.register(command, "List all of your homes", plugin.commands().homes.aliases);
    }

    private int homes(CommandContext<CommandSourceStack> context) {
        var sender = (Player) context.getSource().getSender();
        plugin.homeController().getHomes(sender).thenAccept(homes -> {
            if (homes.isEmpty()) {
                plugin.bundle().sendMessage(sender, "command.home.undefined");
            } else if (plugin.config().guis.homes.enabled) {
                plugin.getServer().getScheduler().runTask(plugin, () -> new HomeGUI(plugin, sender, homes).open());
            } else {
                var list = homes.stream().map(home -> {
                    var event = ClickEvent.runCommand("/home " + home.getName());
                    return Component.text(home.getName())
                            .hoverEvent(HoverEvent.showText(plugin.bundle().component("chat.click.teleport", sender)))
                            .clickEvent(event);
                }).toList();
                plugin.bundle().sendMessage(sender, "command.home.list",
                        Formatter.number("amount", homes.size()),
                        Formatter.joining("homes", list));
            }
        }).exceptionally(throwable -> {
            plugin.getComponentLogger().error("Failed to retrieve homes", throwable);
            return null;
        });
        return Command.SINGLE_SUCCESS;
    }
}
