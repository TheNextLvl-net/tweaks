package net.thenextlvl.tweaks.command.player;

import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class GodCommand extends EntitiesCommand {
    public GodCommand(final TweaksPlugin plugin) {
        super(plugin);
    }

    public void register(final Commands registrar) {
        final var command = create(plugin.commands().god.command, "tweaks.command.god", "tweaks.command.god.others");
        registrar.register(command, "Make you or someone else invulnerable", plugin.commands().god.aliases);
    }

    @Override
    protected void execute(final CommandSender sender, final Entity entity) {
        entity.setInvulnerable(!entity.isInvulnerable());

        final var messageSelf = entity.isInvulnerable() ? "command.god.active.self" : "command.god.inactive.self";
        final var messageOthers = entity.isInvulnerable() ? "command.god.active.others" : "command.god.inactive.others";

        plugin.bundle().sendMessage(entity, messageSelf);
        if (entity != sender) plugin.bundle().sendMessage(sender, messageOthers,
                Placeholder.component("entity", entity.name().hoverEvent(entity.asHoverEvent())));
    }
}
