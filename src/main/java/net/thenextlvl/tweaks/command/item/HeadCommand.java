package net.thenextlvl.tweaks.command.item;

import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.JsonParser;
import core.api.placeholder.Placeholder;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@CommandInfo(
        name = "head",
        usage = "/head [value|player|url] (value)",
        description = "get heads or information about them",
        permission = "tweaks.command.head"
)
public class HeadCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player))
            sender.sendRichMessage(Messages.COMMAND_SENDER.message(Messages.ENGLISH, sender));
        else if (args.length >= 1 && args[0].equalsIgnoreCase("value")) value(player, args);
        else if (args.length >= 1 && args[0].equalsIgnoreCase("player")) player(player, args);
        else if (args.length >= 1 && args[0].equalsIgnoreCase("url")) url(player, args);
        else return false;
        return true;
    }

    private void value(Player player, String[] args) {
        var item = player.getInventory().getItemInMainHand();
        if (args.length < 2) {
            var value = getValue(item);
            if (value != null) player.sendRichMessage(Messages.ITEM_HEAD_VALUE.message(player.locale(), player,
                    Placeholder.of("value", () -> value.substring(0, value.length() * 10 / 100) + "…"),
                    Placeholder.of("full-value", () -> value)));
            else player.sendRichMessage(Messages.ITEM_HEAD_NONE.message(player.locale(), player));
        } else setSkullValue(item, args[1]);
    }

    private void player(Player player, String[] args) {
    }

    private void url(Player player, String[] args) {
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

    private static void setSkullImgURL(ItemStack item, String url) {
        try {
            setSkullValue(item, Base64.getEncoder().encodeToString(("{\"textures\":{\"SKIN\":{\"url\":\"" + new URI(url) + "\"}}}").getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    private static void setSkullValue(ItemStack item, String base64) {
        try {
            Bukkit.getUnsafe().modifyItemStack(item, "{SkullOwner:{Id:\"" + new UUID(base64.hashCode(), base64.hashCode()) + "\",Properties:{textures:[{Value:\"" + base64 + "\"}]}}}");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
