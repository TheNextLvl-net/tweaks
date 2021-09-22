package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.listener.api.command.CommandSource;
import net.nonswag.tnl.listener.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.item.TNLItemType;
import net.nonswag.tnl.listener.api.message.Message;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ItemCommand extends TNLCommand {

    public ItemCommand() {
        super("item", "tnl.item", "i");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (source.isPlayer()) {
            TNLPlayer player = source.player();
            if (args.length >= 1) {
                try {
                    Material material = Material.valueOf(args[0].toUpperCase());
                    if (!isValid(material)) throw new IllegalArgumentException();
                    ItemStack itemStack = new ItemStack(material);
                    String item = material.name().toLowerCase().replace("_", " ");
                    if (args.length >= 2) {
                        try {
                            int lo = 0;
                            int amount = Integer.parseInt(args[1]);
                            for (int i = 0; i < amount; i++) lo += player.getInventory().addItem(itemStack).size();
                            int i = amount - lo;
                            if (i > 1) item = item + "s";
                            if (i > 0) player.sendMessage("%prefix% §aYou received §8(§7x" + i + "§8) §6" + item);
                            else player.sendMessage("%prefix% §cYour inventory is full");
                        } catch (NumberFormatException e) {
                            player.sendMessage("%prefix% §c/item " + material.name().toLowerCase() + " §8(§6Amount§8)");
                        }
                    } else {
                        if (!player.getInventory().addItem(itemStack).isEmpty()) {
                            player.sendMessage("%prefix% §cYour inventory is full");
                        } else player.sendMessage("%prefix% §aYou received a §6" + item);
                    }
                } catch (IllegalArgumentException e) {
                    player.sendMessage("%prefix% §c/item §8[§6Item§8] §8(§6Amount§8)");
                }
            } else player.sendMessage("%prefix% §c/item §8[§6Item§8] §8(§6Amount§8)");
        } else source.sendMessage(Message.PLAYER_COMMAND_EN.getText());
    }

    @Nonnull
    @Override
    protected List<String> suggest(@Nonnull Invocation invocation) {
        List<String> suggestions = new ArrayList<>();
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (source.isPlayer()) {
            if (args.length <= 1) {
                for (Material material : Material.values()) {
                    if (isValid(material)) suggestions.add(material.name().toLowerCase());
                }
            } else if (args.length == 2) {
                try {
                    Material material = Material.valueOf(args[0].toUpperCase());
                    if (isValid(material)) {
                        for (int i = 1; i <= material.getMaxStackSize(); i++) suggestions.add(String.valueOf(i));
                    }
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
        return suggestions;
    }

    private static boolean isValid(@Nonnull Material material) {
        return material.isItem() && !TNLItemType.AIR.matches(material);
    }
}
