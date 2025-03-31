package net.thenextlvl.tweaks.command.item;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class RenameCommand {
    private final TweaksPlugin plugin;

    public RenameCommand(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().rename.command)
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.rename"))
                .then(Commands.argument("name", StringArgumentType.greedyString())
                        .executes(this::rename))
                .build();
        registrar.register(command, "Changes the display name of the item in your hand", plugin.commands().rename.aliases);
    }

    private int rename(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        var item = player.getInventory().getItemInMainHand();

        var text = context.getArgument("name", String.class);
        var name = MiniMessage.miniMessage().deserialize(text.replace("\\t", "  "));

        var success = !name.equals(item.getData(DataComponentTypes.CUSTOM_NAME));
        if (success) item.setData(DataComponentTypes.CUSTOM_NAME, name);
        var message = item.isEmpty() ? "command.hold.item" : success ? "command.item.rename" : "nothing.changed";
        plugin.bundle().sendMessage(player, message);

        return success ? Command.SINGLE_SUCCESS : 0;
    }
}
