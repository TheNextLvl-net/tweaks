package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.listener.api.message.ChatComponent;
import net.nonswag.tnl.listener.api.message.Message;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class PingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (sender instanceof Player) {
            if (args.length >= 1 && sender.hasPermission("tnl.admin")) {
                TNLPlayer arg = TNLPlayer.cast(args[0]);
                if (arg == null) {
                    sender.sendMessage(ChatComponent.getText("%prefix%§4 " + args[0] + " §cis unknown to us"));
                    return false;
                } else {
                    if (sender.getName().equals(arg.getName())) {
                        sender.sendMessage(ChatComponent.getText("%prefix%§a Your ping is §6" + arg.getPing() + "ms"));
                    } else {
                        sender.sendMessage(ChatComponent.getText("%prefix%§6 " + arg.getName() + "'s§a ping is §6" + arg.getPing() + "ms"));
                    }
                }
            } else {
                sender.sendMessage(ChatComponent.getText("%prefix%§a Your ping is §6" + TNLPlayer.cast(((Player) sender)).getPing() + "ms"));
            }
        } else sender.sendMessage(Message.CONSOLE_COMMAND_EN.getText());
        return false;
    }
}
