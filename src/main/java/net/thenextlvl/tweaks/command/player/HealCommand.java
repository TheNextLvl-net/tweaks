package net.thenextlvl.tweaks.command.player;

import core.api.placeholder.Placeholder;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(
        name = "heal",
        usage = "/<command> (player)",
        description = "heal yourself or someone else",
        permission = "tweaks.command.heal"
)
public class HealCommand extends PlayerCommand {
    @Override
    protected void execute(CommandSender sender, Player player) {
        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        player.setHealth(attribute == null ? 20.0 : attribute.getValue());

        player.setFoodLevel(20);
        player.setSaturation(10f);
        player.setExhaustion(0f);

        player.setFireTicks(0);
        player.setArrowsInBody(0);
        player.setBeeStingersInBody(0);
        player.setRemainingAir(player.getMaximumAir());
        player.setFreezeTicks(0);

        player.sendMessage(Messages.RESTORED_HEALTH_SELF.message(player.locale()));
        if (player == sender) return;
        var locale = sender instanceof Player p ? p.locale() : Messages.ENGLISH;
        var placeholder = Placeholder.<CommandSender>of("player", player.getName());
        sender.sendMessage(Messages.RESTORED_HEALTH_OTHERS.message(locale, placeholder));
    }
}
