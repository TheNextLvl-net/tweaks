package net.thenextlvl.tweaks.command.workstation;

import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class SmithingTableCommand {
    public void register(Commands registrar) {
        var literal = Commands.literal("smithing-table")
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.smithing-table"))
                .executes(context -> {
                    ((Player) context.getSource().getSender()).openSmithingTable(null, true);
                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                })
                .build();
        registrar.register(literal, "Open an smithing table", List.of("smithing"));
    }
}
