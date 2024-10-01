package net.thenextlvl.tweaks.command.item;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class LoreCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().lore().command())
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.lore"))
                .then(Commands.literal("append")
                        .then(Commands.argument("text", StringArgumentType.greedyString())
                                .executes(this::append)))
                .then(Commands.literal("set")
                        .then(Commands.argument("text", StringArgumentType.greedyString())
                                .executes(this::set)))
                .then(Commands.literal("unset")
                        .executes(this::unset))
                .build();
        registrar.register(command, "Change the lore of your items", plugin.commands().lore().aliases());
    }

    private int append(CommandContext<CommandSourceStack> context) {
        return modifyLore(context, itemMeta -> {
            var currentLore = Objects.requireNonNullElseGet(itemMeta.lore(),
                    () -> new ArrayList<Component>());
            currentLore.addAll(getLore(context));
            itemMeta.lore(currentLore);
        });
    }

    private int set(CommandContext<CommandSourceStack> context) {
        return modifyLore(context, itemMeta -> itemMeta.lore(getLore(context)));
    }

    private int unset(CommandContext<CommandSourceStack> context) {
        return modifyLore(context, itemMeta -> itemMeta.lore(null));
    }

    private int modifyLore(CommandContext<CommandSourceStack> context, Consumer<? super ItemMeta> consumer) {
        var player = (Player) context.getSource().getSender();
        var item = player.getInventory().getItemInMainHand();

        if (item.getType().isEmpty()) {
            plugin.bundle().sendMessage(player, "command.hold.item");
            return 0;
        }

        var success = item.editMeta(consumer);
        var message = success ? "command.item.lore.success" : "command.item.lore.fail";

        plugin.bundle().sendMessage(player, message);
        return success ? Command.SINGLE_SUCCESS : 0;
    }

    private List<Component> getLore(CommandContext<CommandSourceStack> context) {
        var text = context.getArgument("text", String.class)
                .replace("\\t", "   ");
        return Arrays.stream(text.split("(\\\\n|<br>|<newline>)"))
                .map(plugin.bundle()::deserialize)
                .toList();
    }
}
