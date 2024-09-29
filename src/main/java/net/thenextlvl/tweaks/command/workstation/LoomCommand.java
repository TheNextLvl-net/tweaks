package net.thenextlvl.tweaks.command.workstation;

import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class LoomCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal("loom")
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.loom"))
                .executes(context -> {
                    ((Player) context.getSource().getSender()).openLoom(null, true);
                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                })
                .build();
        registrar.register(command, "Open a loom");
    }
}
