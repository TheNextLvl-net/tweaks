package net.thenextlvl.tweaks.command.player;

import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.CommandSenderException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

@CommandInfo(
        name = "enderchest",
        usage = "/<command> (player)",
        description = "open your own or someone else's enderchest",
        permission = "tweaks.command.enderchest",
        aliases = {"ec"}
)
public class EnderChestCommand extends PlayerCommand {

    @Override
    protected void execute(CommandSender sender, Player target) {
        if (!(sender instanceof Player player)) throw new CommandSenderException();
        player.openInventory(target.getEnderChest());
    }

    @Override
    protected @Nullable String getArgumentPermission(CommandSender sender, Player argument) {
        return sender.equals(argument) ? null : "tweaks.command.enderchest.others";
    }
}
