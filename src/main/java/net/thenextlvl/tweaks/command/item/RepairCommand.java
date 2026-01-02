package net.thenextlvl.tweaks.command.item;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.GameRules;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;

@NullMarked
public class RepairCommand {
    private final TweaksPlugin plugin;

    public RepairCommand(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().repair.command)
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.repair"))
                .then(Commands.literal("all")
                        .executes(this::repairAll))
                .executes(this::repair)
                .build();
        registrar.register(command, "repair your tools", plugin.commands().repair.aliases);
    }

    private int repair(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        var inventory = player.getInventory();

        if (inventory.getItemInMainHand().getType().isAir()) {
            plugin.bundle().sendMessage(player, "command.hold.item");
            return 0;
        }

        var success = repair(inventory.getItemInMainHand());
        var message = success ? "command.item.repaired.success" : "command.item.repaired.fail";

        if (Boolean.TRUE.equals(player.getWorld().getGameRuleValue(GameRules.SEND_COMMAND_FEEDBACK)) || !success)
            plugin.bundle().sendMessage(player, message);
        return success ? Command.SINGLE_SUCCESS : 0;
    }

    private int repairAll(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        var inventory = player.getInventory();
        var success = Arrays.stream(inventory.getContents()).map(this::repair).reduce(false, Boolean::logicalOr);
        if (Boolean.TRUE.equals(player.getWorld().getGameRuleValue(GameRules.SEND_COMMAND_FEEDBACK)) || !success)
            plugin.bundle().sendMessage(player, success ? "command.item.repaired.all" : "command.item.repaired.none");
        return success ? Command.SINGLE_SUCCESS : 0;
    }

    private boolean repair(@Nullable ItemStack item) {
        if (item == null) return false;
        var damage = item.getData(DataComponentTypes.DAMAGE);
        if (damage == null || damage == 0) return false;
        item.resetData(DataComponentTypes.DAMAGE);
        return true;
    }
}
