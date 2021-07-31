package net.nonswag.tnl.tweaks.listeners;

import net.nonswag.tnl.listener.api.message.Message;
import net.nonswag.tnl.listener.api.message.Placeholder;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.tweaks.commands.DeOPCommand;
import net.nonswag.tnl.tweaks.commands.OPCommand;
import net.nonswag.tnl.tweaks.commands.TPSCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class CommandListener implements Listener {

    @EventHandler
    public void onPlayerCommand(@Nonnull PlayerCommandPreprocessEvent event) {
        String[] args = event.getMessage().split(" ");
        TNLPlayer player = TNLPlayer.cast(event.getPlayer());
        if (args[0].equalsIgnoreCase("/tps")) {
            event.setCancelled(true);
            if (event.getPlayer().hasPermission("tnl.tps")) {
                TPSCommand.sendTPS(event.getPlayer());
            } else {
                player.sendMessage(Message.NO_PERMISSION_EN, new Placeholder("permission", "tnl.tps"));
            }
        } else if (args[0].equalsIgnoreCase("/op")) {
            event.setCancelled(true);
            if (event.getPlayer().hasPermission("tnl.rights")) {
                OPCommand.onCommand(event.getPlayer(), Arrays.asList(args).subList(1, args.length).toArray(new String[]{}));
            } else {
                player.sendMessage(Message.NO_PERMISSION_EN, new Placeholder("permission", "tnl.rights"));
            }
        } else if (args[0].equalsIgnoreCase("/deop")) {
            event.setCancelled(true);
            if (event.getPlayer().hasPermission("tnl.rights")) {
                DeOPCommand.onCommand(event.getPlayer(), Arrays.asList(args).subList(1, args.length).toArray(new String[]{}));
            } else {
                player.sendMessage(Message.NO_PERMISSION_EN, new Placeholder("permission", "tnl.rights"));
            }
        }
    }

    @EventHandler
    public void onConsoleCommand(@Nonnull ServerCommandEvent event) {
        String[] args = event.getCommand().split(" ");
        if (args[0].equalsIgnoreCase("tps")) {
            event.setCancelled(true);
            TPSCommand.sendTPS(event.getSender());
        } else if (args[0].equalsIgnoreCase("op")) {
            event.setCancelled(true);
            OPCommand.onCommand(event.getSender(), Arrays.asList(args).subList(1, args.length).toArray(new String[]{}));
        } else if (args[0].equalsIgnoreCase("deop")) {
            event.setCancelled(true);
            DeOPCommand.onCommand(event.getSender(), Arrays.asList(args).subList(1, args.length).toArray(new String[]{}));
        }
    }
}
