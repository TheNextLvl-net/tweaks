package net.thenextlvl.tweaks.command.workstation;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MenuType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CartographyTableCommand {
    private final TweaksPlugin plugin;

    public CartographyTableCommand(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().cartographyTable.command)
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.cartography-table"))
                .executes(context -> {
                    var player = (Player) context.getSource().getSender();
                    MenuType.CARTOGRAPHY_TABLE.create(player).open();
                    return Command.SINGLE_SUCCESS;
                })
                .build();
        registrar.register(command, "Open a cartography table", plugin.commands().cartographyTable.aliases);
    }
}
