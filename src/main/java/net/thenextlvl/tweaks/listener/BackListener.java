package net.thenextlvl.tweaks.listener;

import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.damage.DamageType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jspecify.annotations.NullMarked;

import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.COMMAND;

@NullMarked
public final class BackListener implements Listener {
    private final TweaksPlugin plugin;

    public BackListener(TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        if (!event.hasChangedBlock()) return;
        if (!event.getPlayer().hasPermission("tweaks.command.back")) return;
        if (!event.getPlayer().isOnGround()) return;
        if (!isSafe(event.getFrom().getBlock())) return;
        plugin.backController().setLastSafeLocation(event.getPlayer(), event.getFrom());
    }

    private final BlockFace[] faces = {
            BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST,
            BlockFace.NORTH_EAST, BlockFace.NORTH_WEST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST
    };

    private boolean isSafe(Block block) {
        var down = block.getRelative(BlockFace.DOWN);
        if (isDangerousOn(down)) return false;
        if (isDangerousIn(block) || !block.isPassable()) return false;
        for (var face : faces) if (isDangerousIn(block.getRelative(face))) return false;
        return true;
    }

    private boolean isDangerousOn(Block block) {
        return !block.isCollidable() || switch (block.getType()) {
            case MAGMA_BLOCK, POINTED_DRIPSTONE, CAMPFIRE, SOUL_CAMPFIRE, BIG_DRIPLEAF -> true;
            default -> false;
        };
    }

    private boolean isDangerousIn(Block block) {
        return block.isLiquid() || switch (block.getType()) {
            case END_PORTAL, NETHER_PORTAL, END_GATEWAY, FIRE, SOUL_FIRE, WITHER_ROSE -> true;
            default -> false;
        };
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        if (switch (event.getCause()) { // todo: config entry
            case NETHER_PORTAL, ENDER_PEARL, COMMAND, END_GATEWAY, PLUGIN, SPECTATE, UNKNOWN, END_PORTAL -> false;
            case CONSUMABLE_EFFECT, DISMOUNT, EXIT_BED -> true;
        }) return;

        var player = event.getPlayer();
        if (!player.hasPermission("tweaks.command.back")) return;

        if (event.getCause().equals(COMMAND) && event.getTo().equals(plugin.backController().getLock(player))) {
            plugin.backController().unlock(player);
            return;
        }

        var location = isSafe(event.getFrom().getBlock()) ? event.getFrom()
                : plugin.backController().getLastSafeLocation(player);
        plugin.backController().offerFirst(player, location != null ? location : event.getFrom());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent event) {
        if (event.getDamageSource().getDamageType().equals(DamageType.OUT_OF_WORLD)) return;

        var player = event.getPlayer();
        if (!player.hasPermission("tweaks.command.back")) return;

        var location = player.getLocation();
        plugin.backController().offerFirst(player, location);
        var last = isSafe(location.getBlock()) ? location : plugin.backController().getLastSafeLocation(player);
        plugin.backController().offerFirst(player, last != null ? last : location);
    }
}
