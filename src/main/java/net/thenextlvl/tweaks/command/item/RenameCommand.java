package net.thenextlvl.tweaks.command.item;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.GameRules;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class RenameCommand {
    private final TweaksPlugin plugin;

    public RenameCommand(final TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(final Commands registrar) {
        final var command = Commands.literal(plugin.commands().rename.command)
                .requires(stack -> stack.getSender() instanceof final Player player
                                   && player.hasPermission("tweaks.command.rename"))
                .then(Commands.argument("name", StringArgumentType.greedyString())
                        .executes(this::rename))
                .build();
        registrar.register(command, "Changes the display name of the item in your hand", plugin.commands().rename.aliases);
    }

    private int rename(final CommandContext<CommandSourceStack> context) {
        final var player = (Player) context.getSource().getSender();
        final var item = player.getInventory().getItemInMainHand();

        final var text = context.getArgument("name", String.class);
        final var name = MiniMessage.miniMessage().deserialize(text.replace("\\t", "  "));

        final var success = !name.equals(item.getData(DataComponentTypes.CUSTOM_NAME));
        if (success) item.setData(DataComponentTypes.CUSTOM_NAME, name);
        final var message = item.isEmpty() ? "command.hold.item" : success ? "command.item.rename" : "nothing.changed";
        if (Boolean.TRUE.equals(player.getWorld().getGameRuleValue(GameRules.SEND_COMMAND_FEEDBACK)) || !success)
            plugin.bundle().sendMessage(player, message);

        return success ? Command.SINGLE_SUCCESS : 0;
    }
}
