package net.thenextlvl.tweaks.command.player;

import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class VanishCommand extends EntitiesCommand {
    public VanishCommand(TweaksPlugin plugin) {
        super(plugin);
    }

    public void register(Commands registrar) {
        var command = create(plugin.commands().vanish().command(),
                "tweaks.command.vanish", "tweaks.command.vanish.others");
        registrar.register(command, "Hide yourself or someone else from others",
                plugin.commands().vanish().aliases());
    }

    @Override
    protected void execute(CommandSender sender, Entity entity) {
        entity.setVisibleByDefault(!entity.isVisibleByDefault());
        plugin.getServer().getOnlinePlayers().stream()
                .filter(player -> !entity.equals(player))
                .forEach(player -> {
                    if (entity.isVisibleByDefault()) player.showEntity(plugin, entity);
                    else player.hideEntity(plugin, entity);
                });

        var messageSelf = entity.isVisibleByDefault() ? "command.vanish.disabled.self" : "command.vanish.enabled.self";
        var messageOthers = entity.isVisibleByDefault() ? "command.vanish.disabled.others" : "command.vanish.enabled.others";
        plugin.bundle().sendMessage(entity, messageSelf);
        if (entity != sender) plugin.bundle().sendMessage(sender, messageOthers,
                Placeholder.component("entity", entity.name().hoverEvent(entity.asHoverEvent())));
    }
}
