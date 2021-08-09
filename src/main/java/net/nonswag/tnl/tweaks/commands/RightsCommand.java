package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.listener.api.message.ChatComponent;
import net.nonswag.tnl.listener.api.permission.Permissions;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RightsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("add")) {
                if (args.length >= 2) {
                    TNLPlayer  arg = TNLPlayer.cast(args[1]);
                    if (arg != null) {
                        Permissions permissionManager = arg.getPermissionManager();
                        if (args.length >= 3) {
                            if (!permissionManager.hasPermission(args[2])) {
                                permissionManager.addPermission(args[2]);
                                sender.sendMessage(ChatComponent.getText("%prefix% §6" + arg.getName() + "§a now has §6" + args[2] + "§a permission"));
                            } else {
                                sender.sendMessage(ChatComponent.getText("%prefix%§c Nothing could be changed"));
                            }
                        } else {
                            sender.sendMessage(ChatComponent.getText("%prefix%§c /rights add " + arg.getName() + " §8[§6Permission§8]"));
                        }
                    } else {
                        sender.sendMessage(ChatComponent.getText("%prefix%§c /rights add §8[§6Player§8] §8[§6Permission§8]"));
                    }
                } else {
                    sender.sendMessage(ChatComponent.getText("%prefix%§c /rights add §8[§6Player§8] §8[§6Permission§8]"));
                }
            } else if (args[0].equalsIgnoreCase("remove")) {
                if (args.length >= 2) {
                    TNLPlayer  arg = TNLPlayer.cast(args[1]);
                    if (arg != null) {
                        Permissions permissionManager = arg.getPermissionManager();
                        if (args.length >= 3) {
                            if (permissionManager.hasPermission(args[2])) {
                                permissionManager.removePermission(args[2]);
                                sender.sendMessage(ChatComponent.getText("%prefix% §6" + arg.getName() + "§a no longer has §6" + args[2] + "§a permission"));
                            } else {
                                sender.sendMessage(ChatComponent.getText("%prefix%§c Nothing could be changed"));
                            }
                        } else {
                            sender.sendMessage(ChatComponent.getText("%prefix%§c /rights remove " + arg.getName() + " §8[§6Permission§8]"));
                        }
                    } else {
                        sender.sendMessage(ChatComponent.getText("%prefix%§c /rights remove §8[§6Player§8] §8[§6Permission§8]"));
                    }
                } else {
                    sender.sendMessage(ChatComponent.getText("%prefix%§c /rights remove §8[§6Player§8] §8[§6Permission§8]"));
                }
            } else if (args[0].equalsIgnoreCase("list")) {
                if (args.length >= 2) {
                    TNLPlayer  arg = TNLPlayer.cast(args[1]);
                    if (arg != null) {
                        Permissions permissionManager = arg.getPermissionManager();
                        sender.sendMessage(ChatComponent.getText("%prefix%§7 Permissions§8(§a" + permissionManager.getPermissions().size() + "§8): §6" + String.join("§8, §6", permissionManager.getPermissions())));
                    } else {
                        sender.sendMessage(ChatComponent.getText("%prefix%§c /rights list §8[§6Player§8]"));
                    }
                } else {
                    sender.sendMessage(ChatComponent.getText("%prefix%§c /rights list §8[§6Player§8]"));
                }
            } else {
                sender.sendMessage(ChatComponent.getText("%prefix%§c /rights add §8[§6Player§8] §8[§6Permission§8]"));
                sender.sendMessage(ChatComponent.getText("%prefix%§c /rights remove §8[§6Player§8] §8[§6Permission§8]"));
                sender.sendMessage(ChatComponent.getText("%prefix%§c /rights list §8[§6Player§8]"));
            }
        } else {
            sender.sendMessage(ChatComponent.getText("%prefix%§c /rights add §8[§6Player§8] §8[§6Permission§8]"));
            sender.sendMessage(ChatComponent.getText("%prefix%§c /rights remove §8[§6Player§8] §8[§6Permission§8]"));
            sender.sendMessage(ChatComponent.getText("%prefix%§c /rights list §8[§6Player§8]"));
        }
        return false;
    }
}
