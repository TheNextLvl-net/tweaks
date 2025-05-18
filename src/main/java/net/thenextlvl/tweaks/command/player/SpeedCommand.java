package net.thenextlvl.tweaks.command.player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.attribute.Attribute.*;
import static org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER;

@NullMarked
public class SpeedCommand {
    private final TweaksPlugin plugin;
    private static final NamespacedKey SPEED_KEY = new NamespacedKey("tweaks", "speed");

    public SpeedCommand(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().speed.command)
                .requires(stack -> stack.getSender().hasPermission("tweaks.command.speed"))
                .then(Commands.argument("speed", IntegerArgumentType.integer(-10, 10))
                        .then(speed("fly", SpeedType.FLY))
                        .then(speed("sneak", SpeedType.SNEAK))
                        .then(speed("walk", SpeedType.WALK))
                        .executes(context -> {
                            var sender = context.getSource().getSender();
                            if (sender instanceof Player player) {
                                var type = player.isFlying() ? SpeedType.FLY : SpeedType.WALK;
                                return speed(context, type, new ArrayList<>(List.of(player)));
                            }
                            plugin.bundle().sendMessage(sender, "command.sender");
                            return 0;
                        }))
                .then(Commands.literal("reset")
                        .then(reset("fly", SpeedType.FLY))
                        .then(reset("sneak", SpeedType.SNEAK))
                        .then(reset("walk", SpeedType.WALK))
                        .executes(context -> {
                            var sender = context.getSource().getSender();
                            if (sender instanceof Player player) {
                                var type = player.isFlying() ? SpeedType.FLY : SpeedType.WALK;
                                return reset(context, type, new ArrayList<>(List.of(player)));
                            }
                            plugin.bundle().sendMessage(sender, "command.sender");
                            return 0;
                        }))
                .build();
        registrar.register(command, "Change your own or others fly, sneak or walk speed",
                plugin.commands().speed.aliases);
    }

    private LiteralArgumentBuilder<CommandSourceStack> reset(String literal, SpeedType type) {
        return Commands.literal(literal)
                .then(Commands.argument("targets", ArgumentTypes.entities())
                        .requires(stack -> stack.getSender().hasPermission("tweaks.command.speed.others"))
                        .executes(context -> {
                            var resolver = context.getArgument("targets", EntitySelectorArgumentResolver.class);
                            return reset(context, type, new ArrayList<>(resolver.resolve(context.getSource())));
                        }))
                .executes(context -> {
                    var sender = context.getSource().getSender();
                    if (sender instanceof Player player)
                        return reset(context, type, new ArrayList<>(List.of(player)));
                    plugin.bundle().sendMessage(sender, "command.sender");
                    return 0;
                });
    }

    private LiteralArgumentBuilder<CommandSourceStack> speed(String literal, SpeedType type) {
        return Commands.literal(literal)
                .then(Commands.argument("targets", ArgumentTypes.entities())
                        .requires(stack -> stack.getSender().hasPermission("tweaks.command.speed.others"))
                        .executes(context -> {
                            var resolver = context.getArgument("targets", EntitySelectorArgumentResolver.class);
                            return speed(context, type, new ArrayList<>(resolver.resolve(context.getSource())));
                        }))
                .executes(context -> {
                    var sender = context.getSource().getSender();
                    if (sender instanceof Player player)
                        return speed(context, type, new ArrayList<>(List.of(player)));
                    plugin.bundle().sendMessage(sender, "command.sender");
                    return 0;
                });
    }

    private int reset(CommandContext<CommandSourceStack> context, SpeedType type, List<Entity> entities) {
        var sender = context.getSource().getSender();

        entities.removeIf(entity -> !(entity instanceof Attributable));
        entities.forEach(entity -> {
            var instance = ((Attributable) entity).getAttribute(type.getAttribute());
            if (instance != null) instance.removeModifier(SPEED_KEY);

            if (!type.equals(SpeedType.SNEAK) && entity instanceof Player player) {
                if (type.equals(SpeedType.FLY)) player.setFlySpeed(0.1f);
                if (type.equals(SpeedType.WALK)) player.setWalkSpeed(0.2f);
            }

            plugin.bundle().sendMessage(entity, "command.speed.reset.self");
            if (!entity.equals(sender)) plugin.bundle().sendMessage(sender, "command.speed.reset.others",
                    Placeholder.component("entity", entity.name().hoverEvent(entity.asHoverEvent())));
        });
        return Command.SINGLE_SUCCESS;
    }

    private int speed(CommandContext<CommandSourceStack> context, SpeedType type, List<Entity> entities) {
        var sender = context.getSource().getSender();
        var speed = context.getArgument("speed", int.class) / 10d;

        entities.removeIf(entity -> !(entity instanceof Attributable));
        entities.forEach(entity -> {
            if (!type.equals(SpeedType.SNEAK) && entity instanceof Player player) {
                if (type.equals(SpeedType.FLY)) player.setFlySpeed((float) speed);
                if (type.equals(SpeedType.WALK)) player.setWalkSpeed((float) speed);
            } else {
                var instance = ((Attributable) entity).getAttribute(type.getAttribute());
                if (instance != null) {
                    instance.removeModifier(SPEED_KEY);
                    var value = speed - instance.getBaseValue();
                    var modifier = new AttributeModifier(SPEED_KEY, value, ADD_NUMBER);
                    instance.addTransientModifier(modifier);
                }
            }

            plugin.bundle().sendMessage(entity, type.getMessageSelf(),
                    Formatter.number("speed", speed));
            if (!entity.equals(sender)) plugin.bundle().sendMessage(sender, type.getMessageOther(),
                    Formatter.number("speed", speed),
                    Placeholder.component("entity", entity.name().hoverEvent(entity.asHoverEvent())));
        });
        return Command.SINGLE_SUCCESS;
    }

    private enum SpeedType {
        FLY(FLYING_SPEED, "command.speed.fly.changed.others", "command.speed.fly.changed.self"),
        SNEAK(SNEAKING_SPEED, "command.speed.sneak.changed.others", "command.speed.sneak.changed.self"),
        WALK(MOVEMENT_SPEED, "command.speed.walk.changed.others", "command.speed.walk.changed.self");

        private final Attribute attribute;
        private final String messageOther;
        private final String messageSelf;

        SpeedType(Attribute attribute, String messageOther, String messageSelf) {
            this.attribute = attribute;
            this.messageOther = messageOther;
            this.messageSelf = messageSelf;
        }

        public Attribute getAttribute() {
            return attribute;
        }

        public String getMessageOther() {
            return messageOther;
        }

        public String getMessageSelf() {
            return messageSelf;
        }
    }
}
