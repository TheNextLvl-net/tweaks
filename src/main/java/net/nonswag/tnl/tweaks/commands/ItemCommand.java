package net.nonswag.tnl.tweaks.commands;

import net.nonswag.core.api.command.CommandSource;
import net.nonswag.core.api.command.Invocation;
import net.nonswag.core.api.message.Placeholder;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.listener.api.command.exceptions.InvalidUseException;
import net.nonswag.tnl.listener.api.item.ItemType;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.tweaks.utils.Messages;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemCommand extends TNLCommand {

    public ItemCommand() {
        super("item", "tnl.item", "i");
        setUsage("%prefix% §c/item §8[§6Item§8] §8(§6Amount§8)");
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void execute(Invocation invocation) {
        TNLPlayer player = (TNLPlayer) invocation.source();
        String[] args = invocation.arguments();
        if (args.length < 1) throw new InvalidUseException(this);
        Material material = Material.getMaterial(args[0].toUpperCase());
        if (material == null) throw new InvalidUseException(this);
        if (!isValid(material)) throw new IllegalArgumentException();
        ItemStack itemStack = new ItemStack(material);
        String item = itemStack.getI18NDisplayName();
        if (item == null) item = material.name().toLowerCase().replace("_", " ");
        if (args.length >= 2) {
            try {
                int left = 0;
                int amount = Integer.parseInt(args[1]);
                for (int i = 0; i < amount; i++) {
                    left += player.inventoryManager().getInventory().addItem(itemStack).size();
                }
                int i = amount - left;
                if (i > 1 && !item.endsWith("s")) item += "s";
                if (i > 0) {
                    player.messenger().sendMessage(Messages.RECEIVED_ITEM, new Placeholder("item", item), new Placeholder("amount", i));
                } else player.messenger().sendMessage(Messages.INVENTORY_FULL);
            } catch (NumberFormatException e) {
                player.messenger().sendMessage("%prefix% §c/item " + material.name().toLowerCase() + " §8(§6Amount§8)");
            }
        } else if (player.inventoryManager().getInventory().addItem(itemStack).isEmpty()) {
            player.messenger().sendMessage(Messages.RECEIVED_ONE_ITEM, new Placeholder("item", item));
        } else player.messenger().sendMessage(Messages.INVENTORY_FULL);
    }

    @Override
    public boolean canUse(CommandSource source) {
        return source instanceof TNLPlayer;
    }

    @Override
    protected List<String> suggest(Invocation invocation) {
        List<String> suggestions = new ArrayList<>();
        String[] args = invocation.arguments();
        if (args.length <= 1) {
            for (Material material : Material.values()) {
                if (isValid(material)) suggestions.add(material.name().toLowerCase());
            }
        } else if (args.length == 2) {
            Material material = Material.getMaterial(args[0].toUpperCase());
            if (material == null) return suggestions;
            if (!isValid(material)) return suggestions;
            for (int i = 1; i <= material.getMaxStackSize(); i++) suggestions.add(String.valueOf(i));
        }
        return suggestions;
    }

    private static boolean isValid(Material material) {
        return material.isItem() && !ItemType.AIR.matches(material);
    }
}
