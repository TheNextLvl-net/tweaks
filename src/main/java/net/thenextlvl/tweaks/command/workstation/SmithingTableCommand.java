package net.thenextlvl.tweaks.command.workstation;

import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class SmithingTableCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal("smithing-table")
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.smithing-table"))
                .executes(context -> {
                    ((Player) context.getSource().getSender()).openSmithingTable(null, true);
                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                })
                .build();
        registrar.register(command, "Open an smithing table", List.of("smithing"));
    }
}
