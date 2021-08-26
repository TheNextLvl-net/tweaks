package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.listener.api.command.CommandSource;
import net.nonswag.tnl.listener.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.permission.Permissions;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class RightsCommand extends TNLCommand {

    public RightsCommand() {
        super("rights", "tnl.rights");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("add")) {
                if (args.length >= 2) {
                    TNLPlayer arg = TNLPlayer.cast(args[1]);
                    if (arg != null) {
                        Permissions permissionManager = arg.getPermissionManager();
                        if (args.length >= 3) {
                            if (!permissionManager.hasPermission(args[2])) {
                                permissionManager.addPermission(args[2]);
                                source.sendMessage("%prefix% §6" + arg.getName() + "§a now has §6" + args[2] + "§a permission");
                            } else source.sendMessage("%prefix% §cNothing could be changed");
                        } else source.sendMessage("%prefix% §c/rights add " + arg.getName() + " §8[§6Permission§8]");
                    } else source.sendMessage("%prefix% §c/rights add §8[§6Player§8] §8[§6Permission§8]");
                } else source.sendMessage("%prefix% §c/rights add §8[§6Player§8] §8[§6Permission§8]");
            } else if (args[0].equalsIgnoreCase("remove")) {
                if (args.length >= 2) {
                    TNLPlayer arg = TNLPlayer.cast(args[1]);
                    if (arg != null) {
                        Permissions permissionManager = arg.getPermissionManager();
                        if (args.length >= 3) {
                            if (permissionManager.hasPermission(args[2])) {
                                permissionManager.removePermission(args[2]);
                                source.sendMessage("%prefix% §6" + arg.getName() + "§a no longer has §6" + args[2] + "§a permission");
                            } else source.sendMessage("%prefix% §cNothing could be changed");
                        } else source.sendMessage("%prefix% §c/rights remove " + arg.getName() + " §8[§6Permission§8]");
                    } else source.sendMessage("%prefix% §c/rights remove §8[§6Player§8] §8[§6Permission§8]");
                } else source.sendMessage("%prefix% §c/rights remove §8[§6Player§8] §8[§6Permission§8]");
            } else if (args[0].equalsIgnoreCase("list")) {
                if (args.length >= 2) {
                    TNLPlayer arg = TNLPlayer.cast(args[1]);
                    if (arg != null) {
                        Permissions permissionManager = arg.getPermissionManager();
                        source.sendMessage("%prefix%§7 Permissions§8(§a" + permissionManager.getPermissions().size() + "§8): §6" + String.join("§8, §6", permissionManager.getPermissions()));
                    } else source.sendMessage("%prefix% §c/rights list §8[§6Player§8]");
                } else source.sendMessage("%prefix% §c/rights list §8[§6Player§8]");
            } else {
                source.sendMessage("%prefix% §c/rights add §8[§6Player§8] §8[§6Permission§8]");
                source.sendMessage("%prefix% §c/rights remove §8[§6Player§8] §8[§6Permission§8]");
                source.sendMessage("%prefix% §c/rights list §8[§6Player§8]");
            }
        } else {
            source.sendMessage("%prefix% §c/rights add §8[§6Player§8] §8[§6Permission§8]");
            source.sendMessage("%prefix% §c/rights remove §8[§6Player§8] §8[§6Permission§8]");
            source.sendMessage("%prefix% §c/rights list §8[§6Player§8]");
        }
    }

    @Nonnull
    @Override
    protected List<String> suggest(@Nonnull Invocation invocation) {
        String[] args = invocation.arguments();
        List<String> suggestions = new ArrayList<>();
        if (args.length <= 1) {
            suggestions.add("add");
            suggestions.add("remove");
            suggestions.add("list");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")) {
                for (Player all : Bukkit.getOnlinePlayers()) suggestions.add(all.getName());
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("remove")) {
                TNLPlayer arg = TNLPlayer.cast(args[1]);
                if (arg != null) suggestions.addAll(arg.getPermissionManager().getPermissions());
            }
        }
        return suggestions;
    }
}
