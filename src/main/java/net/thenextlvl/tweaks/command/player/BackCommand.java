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

import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.*;

@SuppressWarnings("UnstableApiUsage")
public class BackCommand implements Listener {
    private final Map<Player, BlockingDeque<Location>> map = new WeakHashMap<>();
    private final String metadataKey = "tweaks-back";
    private final TweaksPlugin plugin;
    private final int defaultBufferSize;

    public BackCommand(TweaksPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.defaultBufferSize = plugin.config().generalConfig().backBufferStackSize();
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

    private int back(CommandContext<CommandSourceStack> context) {
        var player = (Player) context.getSource().getSender();

        var deque = map.get(player);
        var pop = deque != null ? deque.pollFirst() : null;

        if (deque == null || pop == null) {
            plugin.bundle().sendMessage(player, "back.empty");
            return 0;
        }

        player.setMetadata(metadataKey, new FixedMetadataValue(plugin, true));
        player.teleportAsync(pop, COMMAND).thenAccept(success -> {
            if (success) plugin.bundle().sendMessage(player, "back.teleport.success");
            else plugin.bundle().sendMessage(player, "back.teleport.fail");
        });
        return Command.SINGLE_SUCCESS;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.getCause() != PLUGIN && event.getCause() != COMMAND && event.getCause() != UNKNOWN) return;

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

        var deque = map.computeIfAbsent(player, ignored -> new LinkedBlockingDeque<>(defaultBufferSize));
        deque.offerFirst(player.getLocation());
    }
}
