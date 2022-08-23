package net.nonswag.tnl.tweaks.commands.head;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.nonswag.tnl.core.api.command.Invocation;
import net.nonswag.tnl.core.api.message.Message;
import net.nonswag.tnl.core.api.message.Placeholder;
import net.nonswag.tnl.core.api.message.key.MessageKey;
import net.nonswag.tnl.listener.api.command.simple.PlayerSubCommand;
import net.nonswag.tnl.listener.api.item.TNLItem;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.tweaks.Tweaks;
import net.nonswag.tnl.tweaks.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

class Player extends PlayerSubCommand {

    Player() {
        super("player");
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void execute(@Nonnull Invocation invocation) {
        TNLPlayer player = (TNLPlayer) invocation.source();
        String[] args = invocation.arguments();
        if (args.length >= 2) {
            Tweaks.getInstance().async(() -> {
                OfflinePlayer arg = Bukkit.getOfflinePlayer(args[1]);
                if (arg.getName() != null) player.inventoryManager().getInventory().addItem(TNLItem.create(arg));
                else player.messenger().sendMessage(MessageKey.NOT_A_PLAYER, new Placeholder("player", args[1]));
            });
        } else {
            String owner = owner(player);
            if (owner == null) player.messenger().sendMessage(Messages.NOT_HOLDING_PLAYER_HEAD);
            else player.bukkit().sendMessage(Component.text(Message.format("%prefix% §7Owner§8: §6")).
                    append(Component.text("§6" + owner).clickEvent(ClickEvent.copyToClipboard(owner)).
                            hoverEvent(HoverEvent.showText(Component.text("§7Click to copy")))));
        }
    }

    @Nullable
    private String owner(@Nonnull TNLPlayer player) {
        TNLItem item = TNLItem.create(player.inventoryManager().getItemInMainHand());
        if (!(item.getItemMeta() instanceof SkullMeta skull)) return null;
        return skull.getOwningPlayer() != null ? skull.getOwningPlayer().getName() : "No owner found";
    }

    @Override
    public void usage(@Nonnull Invocation invocation) {
        invocation.source().sendMessage("%prefix% §c/head player §8(§6Player§8)");
    }

    @Nonnull
    @Override
    protected List<String> suggest(@Nonnull Invocation invocation) {
        List<String> suggestions = new ArrayList<>();
        if (invocation.arguments().length < 2) return suggestions;
        for (OfflinePlayer all : Bukkit.getOfflinePlayers()) suggestions.add(all.getName());
        return suggestions;
    }
}
