package net.thenextlvl.tweaks.command.workstation;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MenuType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class StonecutterCommand {
    private final TweaksPlugin plugin;

    public StonecutterCommand(final TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(final Commands registrar) {
        final var command = Commands.literal(plugin.commands().stonecutter.command)
                .requires(stack -> stack.getSender() instanceof final Player player
                                   && player.hasPermission("tweaks.command.stonecutter"))
                .executes(context -> {
                    final var player = (Player) context.getSource().getSender();
                    MenuType.STONECUTTER.create(player).open();
                    return Command.SINGLE_SUCCESS;
                })
                .build();
        registrar.register(command, "Open a stonecutter", plugin.commands().stonecutter.aliases);
    }
}
