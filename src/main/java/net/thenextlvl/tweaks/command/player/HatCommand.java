package net.thenextlvl.tweaks.command.player;

import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.CommandSenderException;
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
@RequiredArgsConstructor
public class HatCommand implements CommandExecutor {
    private final TweaksPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player))
            throw new CommandSenderException();

        var inventory = player.getInventory();
        var item = inventory.getItemInMainHand();
        var helmet = inventory.getHelmet();

        if (!item.getType().isEmpty() || helmet != null) {
            player.playSound(player, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.PLAYERS, 1f, 1f);
            plugin.bundle().sendMessage(sender, "hat.equipped");
            inventory.setItemInMainHand(helmet);
            inventory.setHelmet(item);
        } else plugin.bundle().sendMessage(sender, "hold.item");
        return true;
    }
}
