package net.thenextlvl.tweaks.command.item;

import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.JsonParser;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import core.paper.item.ItemBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.datacomponent.DataComponentTypes;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.suggestion.OfflinePlayerSuggestionProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Base64;

@NullMarked
@RequiredArgsConstructor
public class HeadCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().head().command())
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.head"))
                .then(Commands.literal("player")
                        .then(Commands.argument("player", StringArgumentType.word())
                                .suggests(new OfflinePlayerSuggestionProvider(plugin))
                                .executes(this::playerHead))
                        .executes(this::player))
                .then(Commands.literal("url")
                        .then(Commands.argument("url", StringArgumentType.greedyString())
                                .executes(this::urlHead))
                        .executes(this::url))
                .then(Commands.literal("value")
                        .then(Commands.argument("value", StringArgumentType.greedyString())
                                .executes(this::valueHead))
                        .executes(this::value))
                .build();
        registrar.register(command, "Get heads or information about them", plugin.commands().head().aliases());
    }

    private int playerHead(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        var head = ItemBuilder.of(Material.PLAYER_HEAD)
                .profile(context.getArgument("player", String.class));
        player.getInventory().addItem(head.item());
        plugin.bundle().sendMessage(player, "command.item.head.received");
        return Command.SINGLE_SUCCESS;
    }

    private int valueHead(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        var head = ItemBuilder.of(Material.PLAYER_HEAD)
                .profileValue(context.getArgument("value", String.class));
        player.getInventory().addItem(head.item());
        plugin.bundle().sendMessage(player, "command.item.head.received");
        return Command.SINGLE_SUCCESS;
    }

    private int urlHead(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        var head = ItemBuilder.of(Material.PLAYER_HEAD)
                .profileUrl(context.getArgument("url", String.class));
        player.getInventory().addItem(head.item());
        plugin.bundle().sendMessage(player, "command.item.head.received");
        return Command.SINGLE_SUCCESS;
    }

    private int player(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        var owner = getOwner(player.getInventory().getItemInMainHand());
        if (owner != null) plugin.bundle().sendMessage(player, "command.item.head.player",
                Placeholder.parsed("owner", owner));
        else plugin.bundle().sendMessage(player, "command.item.head.none");
        return Command.SINGLE_SUCCESS;
    }

    private int value(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        var value = getValue(player.getInventory().getItemInMainHand());
        if (value != null) plugin.bundle().sendMessage(player, "command.item.head.value",
                Placeholder.parsed("value", value.substring(0, Math.min(value.length(), 30)) + "…"),
                Placeholder.parsed("full_value", value));
        else plugin.bundle().sendMessage(player, "command.item.head.none");
        return Command.SINGLE_SUCCESS;
    }

    private int url(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        var url = getUrl(player.getInventory().getItemInMainHand());
        if (url != null) plugin.bundle().sendMessage(player, "command.item.head.url",
                Placeholder.parsed("url", url.substring(0, Math.min(url.length(), 30)) + "…"),
                Placeholder.parsed("full_url", url));
        else plugin.bundle().sendMessage(player, "command.item.head.none");
        return Command.SINGLE_SUCCESS;
    }

    private static @Nullable String getValue(ItemStack item) {
        var profile = item.getData(DataComponentTypes.PROFILE);
        return profile != null ? profile.properties().stream()
                .filter(property -> property.getName().equalsIgnoreCase("textures"))
                .findFirst()
                .map(ProfileProperty::getValue)
                .orElse(null) : null;
    }

    private @Nullable String getUrl(ItemStack item) {
        var value = getValue(item);
        var json = value != null ? JsonParser.parseString(
                new String(Base64.getDecoder().decode(value))
        ) : null;
        return json != null ? json.getAsJsonObject()
                .getAsJsonObject("textures")
                .getAsJsonObject("SKIN")
                .get("url")
                .getAsString()
                : null;
    }

    private @Nullable String getOwner(ItemStack item) {
        var profile = item.getData(DataComponentTypes.PROFILE);
        return profile != null ? profile.name() : null;
    }
}
