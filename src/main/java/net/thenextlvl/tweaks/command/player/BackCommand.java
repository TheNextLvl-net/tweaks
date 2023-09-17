package net.thenextlvl.tweaks.command.player;

import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.CommandSenderException;
import net.thenextlvl.tweaks.command.api.NoBackLocationException;
import net.thenextlvl.tweaks.util.Messages;
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

import java.util.WeakHashMap;

import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.*;

@CommandInfo(
        name = "back",
        description = "go back to your last position",
        permission = "tweaks.command.back"
)
public class BackCommand implements CommandExecutor, Listener {

    private final String metadataKey = "tweaks-back";
    private final int defaultBufferSize;
    private final WeakHashMap<Player, RingBufferStack<Location>> map = new WeakHashMap<>();
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

        RingBufferStack<Location> stack = map.get(player);
        if (stack == null) throw new NoBackLocationException();
        Location pop = stack.pop();
        if (pop == null) throw new NoBackLocationException();

        player.setMetadata(metadataKey, new FixedMetadataValue(plugin, true));
        player.teleportAsync(pop, COMMAND).thenAccept(success -> {
            var message = success ? Messages.back.teleport.success : Messages.back.teleport.fail;
            player.sendRichMessage(message.message(player.locale(), player));
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

        RingBufferStack<Location> stack = map.getOrDefault(player, new RingBufferStack<>(defaultBufferSize));
        stack.push(event.getFrom());
        map.put(player, stack);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();

        if (player.getLocation().getY() < player.getWorld().getMinHeight())
            return;

        CommandInfo annotation = getClass().getAnnotation(CommandInfo.class);
        if (!player.hasPermission(annotation.permission()))
            return;

        RingBufferStack<Location> stack = map.getOrDefault(player, new RingBufferStack<>(defaultBufferSize));
        stack.push(player.getLocation());
        map.put(player, stack);
    }
}
