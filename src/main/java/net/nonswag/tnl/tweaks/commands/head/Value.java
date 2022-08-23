package net.nonswag.tnl.tweaks.commands.head;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.nonswag.tnl.core.api.command.Invocation;
import net.nonswag.tnl.core.api.message.Message;
import net.nonswag.tnl.listener.api.command.simple.PlayerSubCommand;
import net.nonswag.tnl.listener.api.item.TNLItem;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.tweaks.utils.Messages;
import org.bukkit.Material;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Base64;

class Value extends PlayerSubCommand {

    Value() {
        super("value");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        TNLPlayer player = (TNLPlayer) invocation.source();
        String[] args = invocation.arguments();
        if (args.length >= 2) {
            byte[] decode = Base64.getDecoder().decode(args[1]);
            JsonElement value = new JsonParser().parse(new String(decode));
            if (value.isJsonObject()) {
                JsonObject root = value.getAsJsonObject();
                if (root.has("textures")) {
                    player.inventoryManager().getInventory().addItem(TNLItem.create(Material.PLAYER_HEAD).setSkullValue(args[1]));
                } else player.messenger().sendMessage(Messages.INVALID_VALUE);
            } else player.messenger().sendMessage(Messages.INVALID_VALUE);
        } else {
            String owner = owner(player);
            if (owner == null) player.messenger().sendMessage(Messages.NOT_HOLDING_PLAYER_HEAD);
            else player.bukkit().sendMessage(Component.text(Message.format("%prefix% §7Value§8: §6")).
                    append(Component.text("§6" + (owner.length() > 20 ? owner.substring(0, 20) + "…" : owner)).
                            clickEvent(ClickEvent.copyToClipboard(owner)).
                            hoverEvent(HoverEvent.showText(Component.text("§7Click to copy")))));
        }
    }

    @Nullable
    private String owner(@Nonnull TNLPlayer player) {
        TNLItem item = TNLItem.create(player.inventoryManager().getItemInMainHand());
        if (!(item.getItemMeta() instanceof SkullMeta skull)) return null;
        String value = HeadCommand.value(skull);
        return value == null ? "No textures found" : value;
    }

    @Override
    public void usage(@Nonnull Invocation invocation) {
        invocation.source().sendMessage("%prefix% §c/head value §8(§6Value§8)");
    }
}
