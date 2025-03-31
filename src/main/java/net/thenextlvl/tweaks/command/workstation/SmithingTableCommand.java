package net.thenextlvl.tweaks.command.workstation;

import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
@RequiredArgsConstructor
public class SmithingTableCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().smithingTable().command())
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.smithing-table"))
                .executes(context -> {
                    ((Player) context.getSource().getSender()).openSmithingTable(null, true);
                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                })
                .build();
        registrar.register(command, "Open an smithing table", plugin.commands().smithingTable().aliases());
    }
}
