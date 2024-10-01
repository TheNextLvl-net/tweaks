package net.thenextlvl.tweaks.command.player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;

import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.COMMAND;

@SuppressWarnings("UnstableApiUsage")
public class BackCommand implements Listener {
    private final String metadataKey = "tweaks-back";
    private final TweaksPlugin plugin;

    public BackCommand(TweaksPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    public void register(Commands registrar) {
        var command = Commands.literal(plugin.commands().back().command())
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.back"))
                .executes(this::back)
                .build();
        registrar.register(command, "Go back to your last position", plugin.commands().back().aliases());
    }

    private int back(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();

        var location = plugin.backController().peekFirst(player);

        if (location == null) {
            plugin.bundle().sendMessage(player, "command.back.none");
            return 0;
        }

        player.setMetadata(metadataKey, new FixedMetadataValue(plugin, true));

        plugin.teleportController().teleport(player, location, COMMAND).thenAccept(success -> {
            var message = success ? "command.back" : "command.teleport.cancelled";
            if (success) plugin.backController().remove(player, location);
            plugin.bundle().sendMessage(player, message);
        });
        return Command.SINGLE_SUCCESS;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        if (switch (event.getCause()) { // todo: config entry
            case ENDER_PEARL, COMMAND, PLUGIN, END_PORTAL, SPECTATE, UNKNOWN -> false;
            case NETHER_PORTAL, END_GATEWAY, CHORUS_FRUIT, DISMOUNT, EXIT_BED -> true;
        }) return;

        var player = event.getPlayer();

        if (event.getCause() == COMMAND && !player.getMetadata(metadataKey).isEmpty()) {
            player.removeMetadata(metadataKey, plugin);
            return;
        }

        if (!player.hasPermission("tweaks.command.back")) return;

        plugin.backController().offerFirst(player, event.getFrom());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent event) {
        var player = event.getPlayer();

        if (player.getLocation().getY() < player.getWorld().getMinHeight()) return;
        if (!player.hasPermission("tweaks.command.back")) return;

        plugin.backController().offerFirst(player, player.getLocation());
    }
}
