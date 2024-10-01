package net.thenextlvl.tweaks.command.player;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("UnstableApiUsage")
public class HealCommand extends PlayerCommand {
    public HealCommand(TweaksPlugin plugin) {
        super(plugin);
    }

    public void register(Commands registrar) {
        var command = create(plugin.commands().heal().command(), "tweaks.command.heal", "tweaks.command.heal.others");
        registrar.register(command, "Heal yourself or someone else", plugin.commands().heal().aliases());
    }

    @Override
    protected int execute(CommandSender sender, Player player) {
        var attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        player.setHealth(attribute == null ? 20.0 : attribute.getValue());

        player.setExhaustion(0f);
        player.setFoodLevel(20);
        player.setSaturation(20f);

        player.setArrowsInBody(0);
        player.setBeeStingersInBody(0);
        player.setFireTicks(0);
        player.setFreezeTicks(0);
        player.setRemainingAir(player.getMaximumAir());

        plugin.bundle().sendMessage(player, "command.health.restored.self");
        if (player != sender) plugin.bundle().sendMessage(sender, "command.health.restored.others",
                Placeholder.parsed("player", player.getName()));

        return Command.SINGLE_SUCCESS;
    }
}
