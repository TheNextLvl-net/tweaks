package net.thenextlvl.tweaks.command.item;

import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.CommandSenderException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@CommandInfo(
        name = "unbreakable",
        permission = "tweaks.command.unbreakable",
        description = "Makes the item in your hand unbreakable"
)
@RequiredArgsConstructor
public class UnbreakableCommand implements TabExecutor {
    private final TweaksPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player))
            throw new CommandSenderException();

        if (!player.getInventory().getItemInMainHand().editMeta(itemMeta -> {

            itemMeta.setUnbreakable(!itemMeta.isUnbreakable());

            var message = itemMeta.isUnbreakable() ? "item.unbreakable.success" : "item.unbreakable.removed";
            plugin.bundle().sendMessage(sender, message);

        })) plugin.bundle().sendMessage(player, "item.unbreakable.fail");

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
