package net.thenextlvl.tweaks.command.home;

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
import net.thenextlvl.tweaks.gui.HomeGUI;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class HomesCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().homes().command())
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("extra-tweaks.home"))
                .executes(this::homes)
                .build();
        registrar.register(command, "List all of your homes", plugin.commands().homes().aliases());
    }

    private int homes(CommandContext<CommandSourceStack> context) {
        var sender = (Player) context.getSource().getSender();
        plugin.homeController().getHomes(sender).thenAccept(homes -> {
            if (homes.isEmpty()) {
                plugin.bundle().sendMessage(sender, "command.home.undefined");
            } else if (plugin.config().guis().homes().enabled()) {
                plugin.getServer().getScheduler().runTask(plugin, () -> new HomeGUI(plugin, sender, homes).open());
            } else {
                var list = Component.join(JoinConfiguration.commas(true), homes.stream().map(home -> {
                    var event = ClickEvent.runCommand("/home " + home.getName());
                    return Component.text(home.getName())
                            .hoverEvent(HoverEvent.showText(plugin.bundle().component(sender, "chat.click.teleport")))
                            .clickEvent(event);
                }).toList());
                plugin.bundle().sendMessage(sender, "command.home.list",
                        Placeholder.parsed("amount", String.valueOf(homes.size())),
                        Placeholder.component("homes", list));
            }
        }).exceptionally(throwable -> {
            plugin.getComponentLogger().error("Failed to retrieve homes", throwable);
            return null;
        });
        return Command.SINGLE_SUCCESS;
    }
}
