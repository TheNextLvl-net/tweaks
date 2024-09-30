package net.thenextlvl.tweaks.command.item;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class RenameCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal("rename")
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.rename"))
                .then(Commands.argument("name", StringArgumentType.greedyString())
                        .executes(this::rename))
                .build();
        registrar.register(command, "Changes the display name of the item in your hand");
    }

    private int rename(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        var item = player.getInventory().getItemInMainHand();

        var text = context.getArgument("name", String.class);
        var name = MiniMessage.miniMessage().deserialize(text.replace("\\t", "  "));

        if (!item.editMeta(itemMeta -> itemMeta.displayName(name))) {
            plugin.bundle().sendMessage(player, "command.item.rename.fail");
            return 0;
        }

        plugin.bundle().sendMessage(player, "command.item.rename.success");
        return Command.SINGLE_SUCCESS;
    }
}
