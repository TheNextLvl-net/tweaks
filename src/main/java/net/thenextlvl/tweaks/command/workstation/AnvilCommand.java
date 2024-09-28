package net.thenextlvl.tweaks.command.workstation;

import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class AnvilCommand {
    public void register(Commands registrar) {
        var literal = Commands.literal("anvil")
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.anvil"))
                .executes(context -> {
                    ((Player) context.getSource().getSender()).openAnvil(null, true);
                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                })
                .build();
        registrar.register(literal, "Open an anvil inventory");
    }
}
