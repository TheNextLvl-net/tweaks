package net.thenextlvl.tweaks.command.workstation;

import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class CartographyTableCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var literal = Commands.literal("cartography-table")
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.cartography-table"))
                .executes(context -> {
                    ((Player) context.getSource().getSender()).openCartographyTable(null, true);
                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                })
                .build();
        registrar.register(literal, "Open a cartography table", List.of("cartography"));
    }
}
