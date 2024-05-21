package net.thenextlvl.tweaks.command.player;

import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.CommandSenderException;
import net.thenextlvl.tweaks.command.api.NoBackLocationException;
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

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.*;

@CommandInfo(
        name = "back",
        description = "go back to your last position",
        permission = "tweaks.command.back"
)
public class BackCommand implements CommandExecutor, Listener {

    private final String metadataKey = "tweaks-back";
    private final int defaultBufferSize;
    private final Map<Player, BlockingDeque<Location>> map = new WeakHashMap<>();
    private final TweaksPlugin plugin;

    public BackCommand(TweaksPlugin plugin) {
        this.plugin = plugin;
        this.defaultBufferSize = plugin.config().generalConfig().backBufferStackSize();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player))
            throw new CommandSenderException();

        var deque = map.get(player);
        if (deque == null) throw new NoBackLocationException();

        var pop = deque.pollFirst();
        if (pop == null) throw new NoBackLocationException();

        player.setMetadata(metadataKey, new FixedMetadataValue(plugin, true));
        player.teleportAsync(pop, COMMAND).thenAccept(success -> {
            if (success) plugin.bundle().sendMessage(player, "back.teleport.success");
            else plugin.bundle().sendMessage(player, "back.teleport.fail");
        });
        return true;
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {

        if (event.getCause() != PLUGIN && event.getCause() != COMMAND && event.getCause() != UNKNOWN)
            return;

        var player = event.getPlayer();
        if (event.getCause() == COMMAND) {
            if (!player.getMetadata(metadataKey).isEmpty()) {
                player.removeMetadata(metadataKey, plugin);
                return;
            }
        }

        var annotation = getClass().getAnnotation(CommandInfo.class);
        if (!player.hasPermission(annotation.permission()))
            return;

        map.computeIfAbsent(player, ignored ->
                new LinkedBlockingDeque<>(defaultBufferSize)
        ).offerFirst(event.getFrom());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        var player = event.getPlayer();

        if (player.getLocation().getY() < player.getWorld().getMinHeight())
            return;

        var annotation = getClass().getAnnotation(CommandInfo.class);
        if (!player.hasPermission(annotation.permission()))
            return;

        var deque = map.computeIfAbsent(player, ignored -> new LinkedBlockingDeque<>(defaultBufferSize));
        deque.offerFirst(player.getLocation());
    }
}
