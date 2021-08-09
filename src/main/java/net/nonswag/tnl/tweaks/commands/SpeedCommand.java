package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.listener.api.message.Message;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class SpeedCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (sender instanceof Player) {
            TNLPlayer player = TNLPlayer.cast((Player) sender);
            if (args.length >= 1) {
                try {
                    int value = Integer.parseInt(args[0]);
                    if (value >= 0 && value <= 10) {
                        if (player.isFlying()) player.setFlySpeed(value / 10f);
                        else player.setWalkSpeed(value / 10f);
                        String mode = player.isFlying() ? "fly" : "walk";
                        player.sendMessage("%prefix% §aSet your §8(§7" + mode + "§8)§a speed to §6" + value);
                    } else player.sendMessage("%prefix% §cUse a number between §40§c and §410");
                } catch (Exception e) {
                    player.sendMessage("%prefix% §c/speed §8[§6Speed§8]");
                }
            } else {
                player.sendMessage("%prefix% §c/speed §8[§6Speed§8]");
            }
        } else {
            sender.sendMessage(Message.CONSOLE_COMMAND_EN.getText());
        }
        return false;
    }
}
