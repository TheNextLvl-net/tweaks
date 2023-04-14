package net.thenextlvl.tweaks.command.player;

import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@CommandInfo(name = "hat", permission = "tweaks.command.hat")
public class HatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendPlainMessage(Messages.COMMAND_SENDER.message(Messages.ENGLISH));
            return true;
        }

        var inventory = player.getInventory();
        var item = inventory.getItemInMainHand();
        var helmet = inventory.getHelmet();

        if (!item.getType().isEmpty() || helmet != null) {
            player.playSound(player, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.PLAYERS, 1f, 1f);
            inventory.setItemInMainHand(helmet);
            inventory.setHelmet(item);
            // TODO: Your hat was set to ... .
        } else {
            // TODO: You must hold an item
        }
        return true;
    }
}
