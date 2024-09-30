package net.thenextlvl.tweaks.command.player;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class FlyCommand extends PlayerCommand {
    public FlyCommand(TweaksPlugin plugin) {
        super(plugin);
    }

    public void register(Commands registrar) {
        var command = create("fly", "tweaks.command.fly", "tweaks.command.fly.others");
        registrar.register(command, "Toggle your own or someone else's fly state", List.of("flight"));
    }

    @Override
    protected int execute(CommandSender sender, Player player) {
        player.setAllowFlight(!player.getAllowFlight());
        player.setFlying(player.getAllowFlight());

        var messageSelf = player.getAllowFlight() ? "command.flight.enabled.self" : "command.flight.disabled.self";
        var messageOthers = player.getAllowFlight() ? "command.flight.enabled.others" : "command.flight.disabled.others";

        plugin.bundle().sendMessage(player, messageSelf);
        if (player != sender) plugin.bundle().sendMessage(sender, messageOthers,
                Placeholder.parsed("player", player.getName()));

        return Command.SINGLE_SUCCESS;
    }
}
