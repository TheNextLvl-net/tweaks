package net.thenextlvl.tweaks.command.item;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class UnbreakableCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().unbreakable().command())
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.unbreakable"))
                .executes(this::unbreakable)
                .build();
        registrar.register(command, "Makes the item in your hand unbreakable", plugin.commands().unbreakable().aliases());
    }

    private int unbreakable(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        if (!player.getInventory().getItemInMainHand().editMeta(itemMeta -> {
            itemMeta.setUnbreakable(!itemMeta.isUnbreakable());
            var message = itemMeta.isUnbreakable() ? "command.item.unbreakable.success" : "command.item.unbreakable.removed";
            plugin.bundle().sendMessage(player, message);
        })) plugin.bundle().sendMessage(player, "command.item.unbreakable.fail");
        return Command.SINGLE_SUCCESS;
    }
}
