package net.thenextlvl.tweaks.command.home;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.suggestion.HomeSuggestionProvider;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public class SetHomeCommand {
    private final TweaksPlugin plugin;

    public SetHomeCommand(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().setHome.command)
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.home.set"))
                .then(Commands.argument("name", StringArgumentType.string())
                        .suggests(new HomeSuggestionProvider(plugin))
                        .requires(stack -> stack.getSender().hasPermission("tweaks.command.home.set.named"))
                        .executes(context -> setHome(context, context.getArgument("name", String.class))))
                .executes(context -> setHome(context, plugin.config().homes.unnamedName))
                .build();
        registrar.register(command, "Set a home", plugin.commands().setHome.aliases);
    }

    private int setHome(CommandContext<CommandSourceStack> context, String name) {
        CompletableFuture.runAsync(() -> {
            var player = (Player) context.getSource().getSender();
            if (player.hasPermission("tweaks.command.home.limit.bypass")) setHome(name, player);
            else if (plugin.dataController().hasHome(player, name)) setHome(name, player);
            else {
                var limit = plugin.homeController().getMaxHomeCount(player);
                if (limit < 0) {
                    setHome(name, player);
                    return;
                }
                var count = plugin.dataController().getHomeCount(player);
                if (count >= limit) plugin.bundle().sendMessage(player, "command.home.limit",
                        Placeholder.parsed("limit", String.valueOf(limit)));
                else setHome(name, player);
            }
        });
        return Command.SINGLE_SUCCESS;
    }

    private void setHome(String name, Player player) {
        plugin.dataController().setHome(player, name, player.getLocation());
        plugin.bundle().sendMessage(player, "command.home.set.name", Placeholder.parsed("name", name));
    }
}
