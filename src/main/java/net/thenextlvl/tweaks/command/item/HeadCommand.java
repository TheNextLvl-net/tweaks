package net.thenextlvl.tweaks.command.item;

import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.JsonParser;
import core.api.placeholder.Placeholder;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@CommandInfo(
        name = "head",
        usage = "/<command> [value|player|url] (value)",
        description = "get heads or information about them",
        permission = "tweaks.command.head",
        aliases = {"skull"}
)
public class HeadCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player))
            sender.sendRichMessage(Messages.command.sender.message(Messages.ENGLISH, sender));
        else if (args.length >= 1 && args[0].equalsIgnoreCase("value")) value(player, args);
        else if (args.length >= 1 && args[0].equalsIgnoreCase("url")) url(player, args);
        else if (args.length >= 1 && args[0].equalsIgnoreCase("player")) player(player, args);
        else return false;
        return true;
    }

    private void value(Player player, String[] args) {
        if (args.length < 2) {
            var value = getValue(player.getInventory().getItemInMainHand());
            if (value != null) player.sendRichMessage(Messages.item.head.value.message(player.locale(), player,
                    Placeholder.of("value", () -> value.substring(0, Math.min(value.length(), 30)) + "…"),
                    Placeholder.of("full-value", () -> value)));
            else player.sendRichMessage(Messages.item.head.none.message(player.locale(), player));
        } else {
            player.getInventory().addItem(setValue(new ItemStack(Material.PLAYER_HEAD), args[1]));
            player.sendRichMessage(Messages.item.head.received.message(player.locale(), player));
        }
    }

    private void url(Player player, String[] args) {
        if (args.length < 2) {
            var url = getUrl(player.getInventory().getItemInMainHand());
            if (url != null) player.sendRichMessage(Messages.item.head.url.message(player.locale(), player,
                    Placeholder.of("url", () -> url.substring(0, Math.min(url.length(), 30)) + "…"),
                    Placeholder.of("full-url", () -> url)));
            else player.sendRichMessage(Messages.item.head.none.message(player.locale(), player));
        } else {
            player.getInventory().addItem(setImgURL(new ItemStack(Material.PLAYER_HEAD), args[1]));
            player.sendRichMessage(Messages.item.head.received.message(player.locale(), player));
        }
    }

    private void player(Player player, String[] args) {
        if (args.length < 2) {
            var owner = getOwner(player.getInventory().getItemInMainHand());
            if (owner != null) player.sendRichMessage(Messages.item.head.player.message(player.locale(), player,
                    Placeholder.of("owner", () -> owner)));
            else player.sendRichMessage(Messages.item.head.none.message(player.locale(), player));
        } else {
            player.getInventory().addItem(setOwner(new ItemStack(Material.PLAYER_HEAD), args[1]));
            player.sendRichMessage(Messages.item.head.received.message(player.locale(), player));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        var suggestions = new ArrayList<String>();
        if (args.length <= 1) {
            suggestions.add("player");
            suggestions.add("value");
            suggestions.add("url");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("player")) {
            for (var all : Bukkit.getOfflinePlayers()) suggestions.add(all.getName());
        }
        return suggestions;
    }

    @SuppressWarnings("deprecation")
    private static ItemStack setValue(ItemStack item, String base64) {
        var id = new UUID(base64.hashCode(), base64.hashCode());
        var nbt = "{SkullOwner:{Id:\"" + id + "\",Properties:{textures:[{Value:\"" + base64 + "\"}]}}}";
        return Bukkit.getUnsafe().modifyItemStack(item, nbt);
    }

    private static ItemStack setImgURL(ItemStack item, String url) {
        var link = "https://textures.minecraft.net/texture/";
        if (!url.startsWith(link)) url = link + url;
        var nbt = "{\"textures\":{\"SKIN\":{\"url\":\"" + url + "\"}}}";
        var base64 = Base64.getEncoder().encodeToString(nbt.getBytes());
        return setValue(item, base64);
    }

    private static ItemStack setOwner(ItemStack item, String owner) {
        if (!(item.getItemMeta() instanceof SkullMeta skull)) return item;
        skull.setOwningPlayer(Bukkit.getOfflinePlayer(owner));
        item.setItemMeta(skull);
        return item;
    }

    private static @Nullable String getValue(ItemStack item) {
        if (!(item.getItemMeta() instanceof SkullMeta skull)) return null;
        var profile = skull.getPlayerProfile();
        if (profile == null || !profile.hasTextures()) return null;
        return profile.getProperties().stream()
                .filter(property -> property.getName().equalsIgnoreCase("textures"))
                .findFirst()
                .map(ProfileProperty::getValue)
                .orElse(null);
    }

    private @Nullable String getUrl(ItemStack item) {
        var value = getValue(item);
        var json = value != null ? JsonParser.parseString(
                new String(Base64.getDecoder().decode(value))
        ) : null;
        return json != null
                ? json.getAsJsonObject()
                .getAsJsonObject("textures")
                .getAsJsonObject("SKIN")
                .get("url")
                .getAsString()
                : null;
    }


    private @Nullable String getOwner(ItemStack item) {
        return item.getItemMeta() instanceof SkullMeta skull
                ? skull.getOwningPlayer() != null
                ? skull.getOwningPlayer().getName()
                : null : null;
    }
}
