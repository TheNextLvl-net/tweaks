package net.thenextlvl.tweaks.command.item;

import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.CommandSenderException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@CommandInfo(
        name = "rename",
        usage = "/<command> [text...]",
        permission = "tweaks.command.rename",
        description = "Changes the display name of the item in your hand"
)
@RequiredArgsConstructor
public class RenameCommand implements TabExecutor {
    private final TweaksPlugin plugin;

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player))
            throw new CommandSenderException();
        if (args.length < 1)
            return false;

        var item = player.getInventory().getItemInMainHand();

        var name = ChatColor.translateAlternateColorCodes('&', String.join(" ", args))
                .replace("\\t", "   ");

        if (!item.editMeta(itemMeta -> itemMeta.setDisplayName(name))) {
            plugin.bundle().sendMessage(player, "item.rename.fail");
            return true;
        }

        plugin.bundle().sendMessage(player, "item.rename.success");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
