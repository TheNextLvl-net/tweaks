package net.thenextlvl.tweaks.command.player;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("UnstableApiUsage")
public class GodCommand extends PlayerCommand {
    public GodCommand(TweaksPlugin plugin) {
        super(plugin);
    }

    public void register(Commands registrar) {
        var command = create("god", "tweaks.command.god", "tweaks.command.god.others");
        registrar.register(command, "Make you or someone else invulnerable");
    }

    @Override
    protected int execute(CommandSender sender, Player player) {
        player.setInvulnerable(!player.isInvulnerable());

        var messageSelf = player.isInvulnerable() ? "command.god.active.self" : "command.god.inactive.self";
        var messageOthers = player.isInvulnerable() ? "command.god.active.others" : "command.god.inactive.others";

        plugin.bundle().sendMessage(player, messageSelf);
        if (player != sender) plugin.bundle().sendMessage(sender, messageOthers,
                Placeholder.parsed("player", player.getName()));
        return Command.SINGLE_SUCCESS;
    }
}
