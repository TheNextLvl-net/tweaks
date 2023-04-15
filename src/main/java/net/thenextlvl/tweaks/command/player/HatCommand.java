package net.thenextlvl.tweaks.command.player;

import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.CommandSenderException;
import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(
        name = "hat",
        description = "equip your items as hats",
        permission = "tweaks.command.hat"
)
public class HatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player))
            throw new CommandSenderException();

        var inventory = player.getInventory();
        var item = inventory.getItemInMainHand();
        var helmet = inventory.getHelmet();

        if (!item.getType().isEmpty() || helmet != null) {
            player.playSound(player, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.PLAYERS, 1f, 1f);
            sender.sendPlainMessage(Messages.HAT_EQUIPPED.message(player.locale()));
            inventory.setItemInMainHand(helmet);
            inventory.setHelmet(item);
        } else player.sendPlainMessage(Messages.HAT_EQUIPPED.message(player.locale()));
        return true;
    }
}
