package net.thenextlvl.tweaks.command.player;

import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.CommandSenderException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(
        name = "inventory",
        usage = "/<command> (player)",
        description = "open your own or someone else's inventory",
        permission = "tweaks.command.inventory",
        aliases = {"inv"}
)
public class InventoryCommand extends PlayerCommand {

    @Override
    protected void execute(CommandSender sender, Player target) {
        if (!(sender instanceof Player player)) throw new CommandSenderException();
        player.openInventory(target.getInventory());
    }
}
