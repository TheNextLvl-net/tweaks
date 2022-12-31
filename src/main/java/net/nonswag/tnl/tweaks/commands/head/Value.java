package net.nonswag.tnl.tweaks.commands.head;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.nonswag.core.api.command.Invocation;
import net.nonswag.core.api.file.helper.JsonHelper;
import net.nonswag.core.api.message.Message;
import net.nonswag.tnl.listener.api.command.simple.PlayerSubCommand;
import net.nonswag.tnl.listener.api.item.TNLItem;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.tweaks.commands.errors.InvalidValueException;
import net.nonswag.tnl.tweaks.utils.Messages;
import org.bukkit.Material;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nullable;
import java.util.Base64;

class Value extends PlayerSubCommand {

    Value() {
        super("value", "tnl.head.value");
    }

    @Override
    protected void execute(Invocation invocation) {
        TNLPlayer player = (TNLPlayer) invocation.source();
        String[] args = invocation.arguments();
        if (args.length >= 2) {
            byte[] decode = Base64.getDecoder().decode(args[1]);
            JsonElement value = JsonHelper.parse(new String(decode));
            if (!value.isJsonObject()) throw new InvalidValueException();
            JsonObject root = value.getAsJsonObject();
            if (!root.has("textures")) throw new InvalidValueException();
            player.inventoryManager().getInventory().addItem(TNLItem.create(Material.PLAYER_HEAD).setSkullValue(args[1]));
        } else {
            String owner = owner(player);
            if (owner != null) player.bukkit().sendMessage(Component.text(Message.format("%prefix% §7Value§8: §6")).
                    append(Component.text("§6" + (owner.length() > 20 ? owner.substring(0, 20) + "…" : owner)).
                            clickEvent(ClickEvent.copyToClipboard(owner)).
                            hoverEvent(HoverEvent.showText(Component.text("§7Click to copy")))));
            else player.messenger().sendMessage(Messages.NOT_HOLDING_PLAYER_HEAD);
        }
    }

    @Nullable
    private String owner(TNLPlayer player) {
        TNLItem item = TNLItem.create(player.inventoryManager().getItemInMainHand());
        if (!(item.getItemMeta() instanceof SkullMeta skull)) return null;
        String value = HeadCommand.value(skull);
        return value == null ? "No textures found" : value;
    }

    @Override
    public void usage(Invocation invocation) {
        invocation.source().sendMessage("%prefix% §c/head value §8(§6Value§8)");
    }
}
