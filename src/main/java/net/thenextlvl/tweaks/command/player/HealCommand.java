package net.thenextlvl.tweaks.command.player;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

@CommandInfo(
        name = "heal",
        usage = "/<command> (player)",
        description = "heal yourself or someone else",
        permission = "tweaks.command.heal"
)
@RequiredArgsConstructor
public class HealCommand extends PlayerCommand {
    private final TweaksPlugin plugin;

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

        if (player == sender)
            plugin.bundle().sendMessage(sender, "health.restored.self");
        if (player != sender) {
            plugin.bundle().sendMessage(sender, "health.restored.others",
                    Placeholder.component("player", player.name()));
            plugin.bundle().sendMessage(player, "health.restored.self");
        }
    }

    @Override
    protected @Nullable String getArgumentPermission(CommandSender sender, Player argument) {
        return sender.equals(argument) ? null : "tweaks.command.heal.others";
    }
}
