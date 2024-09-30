package net.thenextlvl.tweaks.command.player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class HatCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal("hat")
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.hat"))
                .executes(this::hat)
                .build();
        registrar.register(command, "Equip an item as a hat");
    }

    private int hat(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        var inventory = player.getInventory();
        var item = inventory.getItemInMainHand();
        var helmet = inventory.getHelmet();

        if (item.getType().isEmpty() && helmet == null) {
            plugin.bundle().sendMessage(player, "command.hold.item");
            return 0;
        }

        player.playSound(player, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.PLAYERS, 1f, 1f);
        plugin.bundle().sendMessage(player, "command.hat.equipped");
        inventory.setItemInMainHand(helmet);
        inventory.setHelmet(item);

        return Command.SINGLE_SUCCESS;
    }
}
