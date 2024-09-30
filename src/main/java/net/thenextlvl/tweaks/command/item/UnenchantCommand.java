package net.thenextlvl.tweaks.command.item;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.suggestion.UnenchantSuggestionProvider;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class UnenchantCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal("unenchant")
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.unenchant"))
                .then(Commands.argument("enchantment", ArgumentTypes.resourceKey(RegistryKey.ENCHANTMENT))
                        .suggests(new UnenchantSuggestionProvider())
                        .executes(this::unenchant))
                .build();
        registrar.register(command, "Unenchant your tools");
    }

    @SuppressWarnings("unchecked")
    private int unenchant(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        var item = player.getInventory().getItemInMainHand();

        var key = (TypedKey<Enchantment>) context.getArgument("enchantment", TypedKey.class);
        var enchantment = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(key);

        if (enchantment == null) {
            plugin.bundle().sendMessage(player, "command.enchantment.invalid",
                    Placeholder.parsed("enchantment", key.key().asString()));
            return 0;
        }

        var level = item.removeEnchantment(enchantment);

        var message = level != 0 ? "command.enchantment.removed" : "command.enchantment.absent";
        plugin.bundle().sendMessage(player, message, Placeholder.component("enchantment",
                enchantment.displayName(level).style(Style.empty())));
        return level != 0 ? Command.SINGLE_SUCCESS : 0;
    }
}
