package net.thenextlvl.tweaks.command.player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.RequiredArgsConstructor;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
@RequiredArgsConstructor
public class SuicideCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().suicide().command())
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.suicide"))
                .executes(this::suicide)
                .build();
        registrar.register(command, "Take your own life", plugin.commands().suicide().aliases());
    }

    private int suicide(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();
        player.damage(player.getHealth(), DamageSource.builder(DamageType.GENERIC_KILL).build());
        plugin.bundle().sendMessage(player, "command.suicide");
        return Command.SINGLE_SUCCESS;
    }
}
