package net.nonswag.tnl.tweaks.commands.head;

import com.google.gson.JsonElement;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.nonswag.core.api.command.Invocation;
import net.nonswag.core.api.file.helper.JsonHelper;
import net.nonswag.core.api.message.Message;
import net.nonswag.tnl.listener.api.command.simple.PlayerSubCommand;
import net.nonswag.tnl.listener.api.item.TNLItem;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.tweaks.utils.Messages;
import org.bukkit.Material;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nullable;
import java.util.Base64;

class URL extends PlayerSubCommand {

    URL() {
        super("url", "tnl.head.url");
    }

    @Override
    protected void execute(Invocation invocation) {
        TNLPlayer player = (TNLPlayer) invocation.source();
        String[] args = invocation.arguments();
        if (args.length >= 2) {
            String url = args[1];
            if (!url.toLowerCase().startsWith("http://") && !url.toLowerCase().startsWith("https://")) {
                url = "http://textures.minecraft.net/texture/" + url;
            }
            player.inventoryManager().getInventory().addItem(TNLItem.create(Material.PLAYER_HEAD).setSkullImgURL(url));
        } else {
            String owner = owner(player);
            if (owner != null) {
                String content = (owner.contains("/") ? "…" + owner.substring(owner.lastIndexOf('/') + 1) : owner);
                content = content.length() > 20 ? content.substring(0, 20) + "…" : content;
                player.bukkit().sendMessage(Component.text(Message.format("%prefix% §7URL§8: §6")).
                        append(Component.text("§6" + content).clickEvent(ClickEvent.openUrl(owner)).
                                hoverEvent(HoverEvent.showText(Component.text("§7Click to open url")))));
            } else player.messenger().sendMessage(Messages.NOT_HOLDING_PLAYER_HEAD);
        }
    }

    @Nullable
    private String owner(TNLPlayer player) {
        TNLItem item = TNLItem.create(player.inventoryManager().getItemInMainHand());
        if (!(item.getItemMeta() instanceof SkullMeta skull)) return null;
        String value = HeadCommand.value(skull);
        String url = null;
        if (value != null) {
            JsonElement json = JsonHelper.parse(new String(Base64.getDecoder().decode(value)));
            url = json.getAsJsonObject().getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
        }
        return url == null ? "No URL found" : url;
    }

    @Override
    public void usage(Invocation invocation) {
        invocation.source().sendMessage("%prefix% §c/head url §8(§6URL§8)");
    }
}
