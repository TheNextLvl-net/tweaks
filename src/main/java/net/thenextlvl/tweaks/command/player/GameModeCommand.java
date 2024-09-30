package net.thenextlvl.tweaks.command.player;

import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.argument.GameModeArgumentType;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class GameModeCommand {
    private final TweaksPlugin plugin;

    public void register(Commands registrar) {
        var command = Commands.literal("gamemode")
                .requires(stack -> stack.getSender().hasPermission("tweaks.command.gamemode"))
                .then(Commands.argument("gamemode", new GameModeArgumentType())
                        .then(Commands.argument("players", ArgumentTypes.players())
                                .requires(stack -> stack.getSender().hasPermission("tweaks.command.gamemode.others"))
                                .executes(context -> {
                                    var players = context.getArgument("players", PlayerSelectorArgumentResolver.class);
                                    return gamemode(context, players.resolve(context.getSource()));
                                }))
                        .executes(context -> {
                            var sender = context.getSource().getSender();
                            if (sender instanceof Player player) return gamemode(context, List.of(player));
                            plugin.bundle().sendMessage(sender, "command.sender");
                            return 0;
                        }))
                .build();
        registrar.register(command, "Change your own or someone else's game mode", List.of("gm"));
    }

    private int gamemode(CommandContext<CommandSourceStack> context, List<Player> resolve) {
        var sender = context.getSource().getSender();
        var gamemode = context.getArgument("gamemode", GameMode.class);
        resolve.forEach(player -> {
            player.setGameMode(gamemode);

            plugin.bundle().sendMessage(player, "command.gamemode.changed.self", Placeholder.component("gamemode",
                    Component.translatable(gamemode)));

            if (!sender.equals(player)) plugin.bundle().sendMessage(sender, "command.gamemode.changed.others",
                    Placeholder.component("gamemode", Component.translatable(gamemode)),
                    Placeholder.parsed("player", player.getName()));
        });
        return 0;
    }
}
