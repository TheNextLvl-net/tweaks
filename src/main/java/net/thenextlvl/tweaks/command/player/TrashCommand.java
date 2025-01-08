package net.thenextlvl.tweaks.command.player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MenuType;
import org.jspecify.annotations.NullMarked;

@NullMarked
@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class TrashCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().trash().command())
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.trash"))
                .executes(this::trash)
                .build();
        registrar.register(command, "Dispose of your unwanted items", plugin.commands().hat().aliases());
    }

    private int trash(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        player.openInventory(MenuType.GENERIC_9X4.create(player,
                plugin.bundle().component(player, "gui.title.trash")
        ));
        return Command.SINGLE_SUCCESS;
    }
}
