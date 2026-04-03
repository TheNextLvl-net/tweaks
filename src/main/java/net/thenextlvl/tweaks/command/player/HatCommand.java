package net.thenextlvl.tweaks.command.player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.GameRules;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class HatCommand {
    private final TweaksPlugin plugin;

    public HatCommand(final TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(final Commands registrar) {
        final var command = Commands.literal(plugin.commands().hat.command)
                .requires(stack -> stack.getSender() instanceof final Player player
                                   && player.hasPermission("tweaks.command.hat"))
                .executes(this::hat)
                .build();
        registrar.register(command, "Equip an item as a hat", plugin.commands().hat.aliases);
    }

    private int hat(final CommandContext<CommandSourceStack> context) {
        final var player = (Player) context.getSource().getSender();
        final var inventory = player.getInventory();
        final var item = inventory.getItemInMainHand();
        final var helmet = inventory.getHelmet();

        if (item.getType().isAir() && helmet == null) {
            plugin.bundle().sendMessage(player, "command.hold.item");
            return 0;
        }

        player.playSound(player, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.PLAYERS, 1f, 1f);
        if (Boolean.TRUE.equals(player.getWorld().getGameRuleValue(GameRules.SEND_COMMAND_FEEDBACK)))
            plugin.bundle().sendMessage(player, "command.hat.equipped");
        inventory.setItemInMainHand(helmet);
        inventory.setHelmet(item);

        return Command.SINGLE_SUCCESS;
    }
}
