package net.thenextlvl.tweaks.command.item;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class RepairCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().repair().command())
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.repair"))
                .then(Commands.literal("all")
                        .executes(this::repairAll))
                .executes(this::repair)
                .build();
        registrar.register(command, "repair your tools", plugin.commands().repair().aliases());
    }

    private int repair(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        var inventory = player.getInventory();

        if (inventory.getItemInMainHand().getType().isEmpty()) {
            plugin.bundle().sendMessage(player, "command.hold.item");
            return 0;
        }

        var success = repair(inventory.getItemInMainHand());
        var message = success ? "command.item.repaired.success" : "command.item.repaired.fail";

        plugin.bundle().sendMessage(player, message);
        return Command.SINGLE_SUCCESS;
    }

    private int repairAll(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        var inventory = player.getInventory();
        for (var item : inventory.getContents()) repair(item);
        plugin.bundle().sendMessage(player, "command.item.repaired.all");
        return Command.SINGLE_SUCCESS;
    }

    private boolean repair(@Nullable ItemStack item) {
        return item != null && item.editMeta(Damageable.class, damageable -> damageable.setDamage(0));
    }
}
