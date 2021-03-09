package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.listener.api.message.ChatComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ThunderCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            ((Player) sender).getWorld().setThundering(true);
        } else {
            for (World world : Bukkit.getWorlds()) {
                world.setThundering(true);
            }
        }
        sender.sendMessage(ChatComponent.getText("%prefix%§a Set the weather to §6Thunder"));
        return true;
    }
}
