package net.thenextlvl.tweaks.command.player;

import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class HealCommand extends EntitiesCommand {
    public HealCommand(TweaksPlugin plugin) {
        super(plugin);
    }

    public void register(Commands registrar) {
        var command = create(plugin.commands().heal.command, "tweaks.command.heal", "tweaks.command.heal.others");
        registrar.register(command, "Heal yourself or someone else", plugin.commands().heal.aliases);
    }

    @Override
    protected void execute(CommandSender sender, Entity entity) {
        if (entity instanceof LivingEntity living) {
            var attribute = living.getAttribute(Attribute.MAX_HEALTH);
            living.setHealth(attribute == null ? 20.0 : attribute.getValue());

            living.setArrowsInBody(0);
            living.setBeeStingersInBody(0);
            living.setRemainingAir(living.getMaximumAir());
        }

        if (entity instanceof Player player) {
            player.setExhaustion(0f);
            player.setFoodLevel(20);
            player.setSaturation(20f);
        }

        entity.setFireTicks(0);
        entity.setFreezeTicks(0);

        plugin.bundle().sendMessage(entity, "command.health.restored.self");
        if (entity != sender) plugin.bundle().sendMessage(sender, "command.health.restored.others",
                Placeholder.component("entity", entity.name().hoverEvent(entity.asHoverEvent())));
    }
}
