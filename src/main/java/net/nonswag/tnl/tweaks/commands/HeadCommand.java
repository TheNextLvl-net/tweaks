package net.nonswag.tnl.tweaks.commands;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.nonswag.tnl.listener.api.command.CommandSource;
import net.nonswag.tnl.listener.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.item.TNLItem;
import net.nonswag.tnl.listener.api.language.MessageKey;
import net.nonswag.tnl.listener.api.message.Message;
import net.nonswag.tnl.listener.api.message.Placeholder;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

import javax.annotation.Nonnull;
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
            TNLPlayer player = source.player();
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("player")) {
                    if (args.length >= 2) {
                        new Thread(() -> {
                            OfflinePlayer arg = Bukkit.getOfflinePlayer(args[1]);
                            if (arg.getName() != null) player.getInventory().addItem(TNLItem.create(arg));
                            else player.sendMessage(MessageKey.NOT_A_PLAYER, new Placeholder("player", args[1]));
                        }).start();
                    } else player.sendMessage("%prefix% §c/head player §8[§6Player§8]");
                } else if (args[0].equalsIgnoreCase("value")) {
                    if (args.length >= 2) {
                        try {
                            byte[] decode = Base64.getDecoder().decode(args[1]);
                            JsonElement value = new JsonParser().parse(new String(decode));
                            if (value.isJsonObject()) {
                                JsonObject root = value.getAsJsonObject();
                                if (root.has("textures")) {
                                    player.getInventory().addItem(TNLItem.create(Material.PLAYER_HEAD).setSkullValue(args[1]));
                                } else throw new IllegalArgumentException();
                            } else throw new IllegalArgumentException();
                        } catch (Exception e) {
                            player.sendMessage("%prefix% §cThe provided value is invalid");
                        }
                    } else player.sendMessage("%prefix% §c/head value §8[§6Value§8]");
                } else if (args[0].equalsIgnoreCase("url")) {
                    if (args.length >= 2) {
                        String url = args[1];
                        if (!url.toLowerCase().startsWith("http://") && !url.toLowerCase().startsWith("https://")) {
                            url = "http://textures.minecraft.net/texture/" + url;
                        }
                        player.getInventory().addItem(TNLItem.create(Material.PLAYER_HEAD).setSkullImgURL(url));
                    } else player.sendMessage("%prefix% §c/head url §8[§6URL§8]");
                } else help(player);
            } else help(player);
        } else source.sendMessage(Message.PLAYER_COMMAND_EN.getText());
    }

    public void help(@Nonnull TNLPlayer player) {
        player.sendMessage("%prefix% §c/head player §8[§6Player§8]");
        player.sendMessage("%prefix% §c/head value §8[§6Value§8]");
        player.sendMessage("%prefix% §c/head url §8[§6URL§8]");
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
