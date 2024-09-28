package net.thenextlvl.tweaks.command.workstation;

import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class StonecutterCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var literal = Commands.literal("stonecutter")
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.stonecutter"))
                .executes(context -> {
                    ((Player) context.getSource().getSender()).openStonecutter(null, true);
                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                })
                .build();
        registrar.register(literal, "Open a stonecutter");
    }
}
