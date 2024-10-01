package net.thenextlvl.tweaks.command.player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.COMMAND;

@SuppressWarnings("UnstableApiUsage")
public class BackCommand implements Listener {
    private final Map<Player, BlockingDeque<Location>> map = new WeakHashMap<>();
    private final String metadataKey = "tweaks-back";
    private final TweaksPlugin plugin;
    private final int defaultBufferSize;

    public BackCommand(TweaksPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.defaultBufferSize = plugin.config().general().backBufferStackSize();
        this.plugin = plugin;
    }

    public void register(Commands registrar) {
        var command = Commands.literal("back")
                .requires(stack -> stack.getSender() instanceof Player player
                                   && player.hasPermission("tweaks.command.back"))
                .executes(this::back)
                .build();
        registrar.register(command, "Go back to your last position");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private int back(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();

        var deque = map.get(player);
        var peek = deque != null ? deque.peekFirst() : null;

        if (deque == null || peek == null) {
            plugin.bundle().sendMessage(player, "command.back.none");
            return 0;
        }

        player.setMetadata(metadataKey, new FixedMetadataValue(plugin, true));

        plugin.teleportController().teleport(player, peek, COMMAND).thenAccept(success -> {
            var message = success ? "command.back" : "command.teleport.cancelled";
            plugin.bundle().sendMessage(player, message);
            if (success) deque.remove(peek);
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

        map.computeIfAbsent(player, ignored ->
                new LinkedBlockingDeque<>(defaultBufferSize)
        ).offerFirst(event.getFrom());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent event) {
        var player = event.getPlayer();

        if (player.getLocation().getY() < player.getWorld().getMinHeight()) return;
        if (!player.hasPermission("tweaks.command.back")) return;

        map.computeIfAbsent(player, ignored ->
                new LinkedBlockingDeque<>(defaultBufferSize)
        ).offerFirst(player.getLocation());
    }
}
