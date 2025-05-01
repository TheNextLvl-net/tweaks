package net.thenextlvl.tweaks.command.item;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class UnbreakableCommand {
    private final TweaksPlugin plugin;

    public UnbreakableCommand(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().unbreakable.command)
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.unbreakable"))
                .executes(this::unbreakable)
                .build();
        registrar.register(command, "Makes the item in your hand unbreakable", plugin.commands().unbreakable.aliases);
    }

    private int unbreakable(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        var item = player.getInventory().getItemInMainHand();
        if (!item.hasData(DataComponentTypes.DAMAGE)) {
            plugin.bundle().sendMessage(player, "command.item.unbreakable.fail");
            return 0;
        } else if (item.hasData(DataComponentTypes.UNBREAKABLE)) {
            item.resetData(DataComponentTypes.UNBREAKABLE);
            plugin.bundle().sendMessage(player, "command.item.unbreakable.removed");
        } else {
            item.setData(DataComponentTypes.UNBREAKABLE);
            plugin.bundle().sendMessage(player, "command.item.unbreakable.success");
        }
        return Command.SINGLE_SUCCESS;
    }
}
