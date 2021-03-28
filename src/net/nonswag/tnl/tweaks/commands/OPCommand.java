package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.listener.api.message.ChatComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class OPCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        onCommand(sender, args);
        return false;
    }

    public static void onCommand(@Nonnull CommandSender sender, @Nonnull String[] args) {
        if (args.length >= 1) {
            Player arg = Bukkit.getPlayer(args[0]);
            if (arg != null) {
                if (!arg.isOp()) {
                    arg.setOp(true);
                    sender.sendMessage(ChatComponent.getText("%prefix% §6" + arg.getName() + "§a is now an operator"));
                } else {
                    sender.sendMessage(ChatComponent.getText("%prefix% §cNothing could be changed"));
                }
            } else {
                sender.sendMessage(ChatComponent.getText("%prefix% §c/op §8[§6Player§8]"));
            }
        } else {
            sender.sendMessage(ChatComponent.getText("%prefix% §c/op §8[§6Player§8]"));
        }
    }
}
