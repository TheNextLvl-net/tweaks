package net.thenextlvl.tweaks.command.workstation;

import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.CommandSenderException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(
        name = "workbench",
        description = "open a virtual workbench",
        permission = "tweaks.command.workbench",
        aliases = "wb"
)
public class WorkbenchCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) throw new CommandSenderException();
        player.openWorkbench(null, true);
        return true;
    }
}
