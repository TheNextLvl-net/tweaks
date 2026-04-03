package net.thenextlvl.tweaks.command.workstation;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MenuType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class EnchantingTableCommand {
    private final TweaksPlugin plugin;

    public EnchantingTableCommand(final TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(final Commands registrar) {
        final var command = Commands.literal(plugin.commands().enchantingTable.command)
                .requires(stack -> stack.getSender() instanceof final Player player
                                   && player.hasPermission("tweaks.command.enchanting-table"))
                .executes(context -> {
                    final var player = (Player) context.getSource().getSender();
                    MenuType.ENCHANTMENT.create(player).open();
                    return Command.SINGLE_SUCCESS;
                })
                .build();
        registrar.register(command, "Open an enchanting table", plugin.commands().enchantingTable.aliases);
    }
}
