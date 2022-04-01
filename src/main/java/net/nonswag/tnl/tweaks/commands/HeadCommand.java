package net.nonswag.tnl.tweaks.commands;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.nonswag.tnl.core.api.command.CommandSource;
import net.nonswag.tnl.core.api.command.Invocation;
import net.nonswag.tnl.core.api.file.helper.JsonHelper;
import net.nonswag.tnl.core.api.message.Message;
import net.nonswag.tnl.core.api.message.Placeholder;
import net.nonswag.tnl.core.api.message.key.MessageKey;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.command.exceptions.SourceMismatchException;
import net.nonswag.tnl.listener.api.item.TNLItem;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class HeadCommand extends TNLCommand {

    public HeadCommand() {
        super("head", "tnl.head");
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (source.isPlayer()) {
            TNLPlayer player = (TNLPlayer) source.player();
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("player")) {
                    if (args.length >= 2) {
                        new Thread(() -> {
                            OfflinePlayer arg = Bukkit.getOfflinePlayer(args[1]);
                            if (arg.getName() != null) {
                                player.inventoryManager().getInventory().addItem(TNLItem.create(arg));
                            } else {
                                player.messenger().sendMessage(MessageKey.NOT_A_PLAYER, new Placeholder("player", args[1]));
                            }
                        }).start();
                    } else {
                        String owner = owner(player, 1);
                        if (owner != null) {
                            player.bukkit().sendMessage(Component.text(Message.format("%prefix% §7Owner§8: §6")).
                                    append(Component.text("§6" + owner).clickEvent(ClickEvent.copyToClipboard(owner)).
                                            hoverEvent(HoverEvent.showText(Component.text("§7Click to copy")))));
                        } else player.messenger().sendMessage("%prefix% §cYou are not holding a player head");
                    }
                } else if (args[0].equalsIgnoreCase("url")) {
                    if (args.length >= 2) {
                        String url = args[1];
                        if (!url.toLowerCase().startsWith("http://") && !url.toLowerCase().startsWith("https://")) {
                            url = "http://textures.minecraft.net/texture/" + url;
                        }
                        player.inventoryManager().getInventory().addItem(TNLItem.create(Material.PLAYER_HEAD).setSkullImgURL(url));
                    } else {
                        String owner = owner(player, 2);
                        if (owner != null) {
                            String content = (owner.contains("/") ? "…" + owner.substring(owner.lastIndexOf('/') + 1) : owner);
                            content = content.length() > 20 ? content.substring(0, 20) + "…" : content;
                            player.bukkit().sendMessage(Component.text(Message.format("%prefix% §7URL§8: §6")).
                                    append(Component.text("§6" + content).clickEvent(ClickEvent.copyToClipboard(owner)).
                                            hoverEvent(HoverEvent.showText(Component.text("§7Click to copy")))));
                        } else player.messenger().sendMessage("%prefix% §cYou are not holding a player head");
                    }
                } else if (args[0].equalsIgnoreCase("value")) {
                    if (args.length >= 2) {
                        byte[] decode = Base64.getDecoder().decode(args[1]);
                        JsonElement value = new JsonParser().parse(new String(decode));
                        if (value.isJsonObject()) {
                            JsonObject root = value.getAsJsonObject();
                            if (root.has("textures")) {
                                player.inventoryManager().getInventory().addItem(TNLItem.create(Material.PLAYER_HEAD).setSkullValue(args[1]));
                            } else player.messenger().sendMessage("%prefix% §cThe provided value is invalid");
                        } else player.messenger().sendMessage("%prefix% §cThe provided value is invalid");
                    } else {
                        String owner = owner(player, 3);
                        if (owner != null) {
                            player.bukkit().sendMessage(Component.text(Message.format("%prefix% §7Value§8: §6")).
                                    append(Component.text("§6" + (owner.length() > 20 ? owner.substring(0, 20) + "…" : owner)).
                                            clickEvent(ClickEvent.copyToClipboard(owner)).
                                            hoverEvent(HoverEvent.showText(Component.text("§7Click to copy")))));
                        } else player.messenger().sendMessage("%prefix% §cYou are not holding a player head");
                    }
                } else help(player);
            } else help(player);
        } else throw new SourceMismatchException();
    }

    @Nullable
    private String owner(@Nonnull TNLPlayer player, int type) {
        TNLItem item = TNLItem.create(player.inventoryManager().getItemInMainHand());
        if (!(item.getItemMeta() instanceof SkullMeta skull)) return null;
        if (type == 1) {
            return skull.getOwningPlayer() != null ? skull.getOwningPlayer().getName() : "No owner found";
        } else if (type == 2) {
            String value = value(skull);
            String url = null;
            if (value != null) {
                JsonElement json = JsonHelper.parse(new String(Base64.getDecoder().decode(value)));
                url = json.getAsJsonObject().getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
            }
            return url == null ? "No URL found" : url;
        } else if (type == 3) {
            String value = value(skull);
            return value == null ? "No textures found" : value;
        }
        return null;
    }

    @Nullable
    private String value(@Nonnull SkullMeta skull) {
        PlayerProfile profile = skull.getPlayerProfile();
        if (profile == null || !profile.hasTextures()) return null;
        for (ProfileProperty property : profile.getProperties()) {
            if (!property.getName().equalsIgnoreCase("textures")) continue;
            System.out.println(property.getSignature());
            return property.getValue();
        }
        return null;
    }

    public void help(@Nonnull TNLPlayer player) {
        player.messenger().sendMessage("%prefix% §c/head player §8(§6Player§8)");
        player.messenger().sendMessage("%prefix% §c/head value §8(§6Value§8)");
        player.messenger().sendMessage("%prefix% §c/head url §8(§6URL§8)");
    }

    @Nonnull
    @Override
    protected List<String> suggest(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        List<String> suggestions = new ArrayList<>();
        if (source.isPlayer()) {
            if (args.length <= 1) {
                suggestions.add("url");
                suggestions.add("player");
                suggestions.add("value");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("player")) {
                    for (OfflinePlayer all : Bukkit.getOfflinePlayers()) suggestions.add(all.getName());
                }
            }
        }
        return suggestions;
    }
}
