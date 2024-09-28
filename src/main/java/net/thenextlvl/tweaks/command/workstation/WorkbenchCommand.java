package net.thenextlvl.tweaks.command.workstation;

import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class WorkbenchCommand {
    public void register(Commands registrar) {
        var literal = Commands.literal("workbench")
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.workbench"))
                .executes(context -> {
                    ((Player) context.getSource().getSender()).openWorkbench(null, true);
                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                })
                .build();
        registrar.register(literal, "Open a workbench", List.of("wb"));
    }
}
