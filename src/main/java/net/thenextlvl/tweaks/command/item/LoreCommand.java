package net.thenextlvl.tweaks.command.item;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.GameRules;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@NullMarked
public class LoreCommand {
    private final TweaksPlugin plugin;

    public LoreCommand(final TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(final Commands registrar) {
        final var command = Commands.literal(plugin.commands().lore.command)
                .requires(stack -> stack.getSender() instanceof final Player player
                                   && player.hasPermission("tweaks.command.lore"))
                .then(Commands.literal("clear").executes(this::clear))
                .then(modify("append", this::append))
                .then(modify("prepend", this::prepend))
                .then(modify("set", this::set))
                .then(replace())
                .build();
        registrar.register(command, "Change the lore of your items", plugin.commands().lore.aliases);
    }

    private LiteralArgumentBuilder<CommandSourceStack> replace() {
        return Commands.literal("replace")
                .then(Commands.argument("text", StringArgumentType.string())
                        .then(Commands.argument("replacement", StringArgumentType.greedyString())
                                .executes(this::replace)));
    }

    private LiteralArgumentBuilder<CommandSourceStack> modify(final String literal, final Command<CommandSourceStack> command) {
        return Commands.literal(literal)
                .then(Commands.argument("text", StringArgumentType.greedyString())
                        .executes(command));
    }

    private int replace(final CommandContext<CommandSourceStack> context) {
        return modifyLore(context, item -> {
            final var data = item.getData(DataComponentTypes.LORE);
            if (data == null || data.lines().isEmpty()) return false;
            final var lore = new ArrayList<>(data.lines());
            final var text = context.getArgument("text", String.class);
            final var replacement = context.getArgument("replacement", String.class);
            final var config = TextReplacementConfig.builder()
                    .matchLiteral(text)
                    .replacement(MiniMessage.miniMessage().deserialize(replacement))
                    .build();
            lore.replaceAll(component -> component.replaceText(config));
            item.lore(lore);
            return !Objects.equals(data, item.getData(DataComponentTypes.LORE));
        });
    }

    private int append(final CommandContext<CommandSourceStack> context) {
        return modifyLore(context, item -> {
            final var lore = new ArrayList<Component>();
            Optional.ofNullable(item.getData(DataComponentTypes.LORE))
                    .map(ItemLore::lines)
                    .ifPresent(lore::addAll);
            lore.addAll(getLore(context));
            item.lore(lore);
            return true;
        });
    }

    private int prepend(final CommandContext<CommandSourceStack> context) {
        return modifyLore(context, item -> {
            final var lore = new ArrayList<>(getLore(context));
            Optional.ofNullable(item.getData(DataComponentTypes.LORE))
                    .map(ItemLore::lines)
                    .ifPresent(lore::addAll);
            item.lore(lore);
            return true;
        });
    }

    private int set(final CommandContext<CommandSourceStack> context) {
        return modifyLore(context, item -> {
            final var lore = getLore(context);
            final var data = item.getData(DataComponentTypes.LORE);
            if (data != null && data.lines().equals(lore)) return false;
            item.lore(lore);
            return true;
        });
    }

    private int clear(final CommandContext<CommandSourceStack> context) {
        return modifyLore(context, item -> {
            final var lore = item.getData(DataComponentTypes.LORE);
            if (lore == null || lore.lines().isEmpty()) return false;
            item.resetData(DataComponentTypes.LORE);
            return true;
        });
    }

    private int modifyLore(final CommandContext<CommandSourceStack> context, final Function<ItemStack, Boolean> function) {
        final var player = (Player) context.getSource().getSender();
        final var item = player.getInventory().getItemInMainHand();

        final var success = !item.isEmpty() && function.apply(item);
        final var message = item.isEmpty() ? "command.hold.item" : success ? "command.item.lore" : "nothing.changed";

        if (Boolean.TRUE.equals(player.getWorld().getGameRuleValue(GameRules.SEND_COMMAND_FEEDBACK)) || !success)
            plugin.bundle().sendMessage(player, message);
        return success ? Command.SINGLE_SUCCESS : 0;
    }

    private List<Component> getLore(final CommandContext<CommandSourceStack> context) {
        final var text = context.getArgument("text", String.class)
                .replace("\\t", "   ");
        return Arrays.stream(text.split("(\\\\n|<br>|<newline>)"))
                .map(MiniMessage.miniMessage()::deserialize)
                .collect(Collectors.toList());
    }
}
