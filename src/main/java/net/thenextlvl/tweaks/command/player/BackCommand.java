package net.thenextlvl.tweaks.command.player;

import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.util.RingBufferStack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.WeakHashMap;

import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.*;

@CommandInfo(name = "back", permission = "tweaks.command.back")
public class BackCommand implements CommandExecutor, Listener {


    private final String metadataKey = "tweaks-back";
    private final int size = 5;
    private final WeakHashMap<Player, RingBufferStack<Location>> map = new WeakHashMap<>();
    private final TweaksPlugin plugin;

    public BackCommand(TweaksPlugin tweaksPlugin) {
        this.plugin = tweaksPlugin;
        Bukkit.getPluginManager().registerEvents(this, tweaksPlugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            // TODO: Have to be online
            return true;
        }

        RingBufferStack<Location> stack = map.get(player);
        Location pop = stack.pop();
        if (pop == null) {
            // TODO: No locations
            return true;
        }

        player.setMetadata(metadataKey, new FixedMetadataValue(plugin, true));
        player.teleportAsync(pop, COMMAND).thenRun(() -> {
            // TODO: Send message
        });
        return true;
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {

        if (event.getCause() != PLUGIN && event.getCause() != COMMAND && event.getCause() != UNKNOWN)
            return;

        Player player = event.getPlayer();
        if (event.getCause() == COMMAND) {
            if (!player.getMetadata(metadataKey).isEmpty()) {
                player.removeMetadata(metadataKey, plugin);
                return;
            }
        }

        CommandInfo annotation = getClass().getAnnotation(CommandInfo.class);
        if (!player.hasPermission(annotation.permission()))
            return;

        RingBufferStack<Location> stack = map.getOrDefault(player, new RingBufferStack<>(size));
        stack.push(event.getTo());
        map.put(player, stack);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();

        CommandInfo annotation = getClass().getAnnotation(CommandInfo.class);
        if (!player.hasPermission(annotation.permission()))
            return;

        RingBufferStack<Location> stack = map.getOrDefault(player, new RingBufferStack<>(size));
        stack.push(player.getLocation());
        map.put(player, stack);
    }
}
