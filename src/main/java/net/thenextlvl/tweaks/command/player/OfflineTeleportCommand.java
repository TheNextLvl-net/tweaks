package net.thenextlvl.tweaks.command.player;

import com.google.gson.annotations.SerializedName;
import core.io.IO;
import core.nbt.file.NBTFile;
import core.nbt.snbt.SNBT;
import core.nbt.snbt.SNBTBuilder;
import core.nbt.tag.CompoundTag;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.CommandSenderException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.COMMAND;

@CommandInfo(
        name = "tpo",
        usage = "/<command> [player] (player)",
        description = "teleport offline-players to others or you to them",
        permission = "tweaks.command.offline-tp"
)
@RequiredArgsConstructor
public class OfflineTeleportCommand implements TabExecutor {
    private final TweaksPlugin plugin;
    private final SNBT snbt = new SNBTBuilder()
            .create();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player))
            throw new CommandSenderException();
        if (args.length == 0 || args.length > 2)
            return false;
        new Thread(() -> {
            var source = Bukkit.getOfflinePlayer(args[0]);
            if (args.length == 1) {
                var location = getLocation(source);
                var message = location != null ? "offline.teleport.success.to" : "offline.teleport.fail.to";
                if (location != null) player.teleportAsync(location, COMMAND);
                plugin.bundle().sendMessage(player, message, Placeholder.parsed("player",
                        String.valueOf(source.getName())));
            } else if (!args[0].equalsIgnoreCase(args[1])) {
                var target = Bukkit.getOfflinePlayer(args[1]);
                plugin.bundle().sendMessage(player, teleport(source, target),
                        Placeholder.parsed("source", String.valueOf(source.getName())),
                        Placeholder.parsed("target", String.valueOf(target.getName())));
            } else plugin.bundle().sendMessage(player, "offline.teleport.location",
                    Placeholder.parsed("player", String.valueOf(source.getName())));
        }, "otp-thread").start();
        return true;
    }

    private String teleport(OfflinePlayer source, OfflinePlayer target) {
        var online = source.getPlayer();
        var location = getLocation(target);
        if (location == null) return "offline.teleport.fail";
        if (online == null) return setLocation(source, location)
                ? "offline.teleport.success" : "offline.teleport.fail";
        if (online.getLocation().equals(location))
            return "offline.teleport.location";
        online.teleportAsync(location, COMMAND);
        return "offline.teleport.success";
    }

    private boolean setLocation(OfflinePlayer player, Location location) {
        var file = getNBTFile(player);
        if (file == null) return false;
        file.getRoot().addAll(snbt
                .toTag(LocationData.of(location))
                .getAsCompound());
        file.save();
        return true;
    }

    private record LocationData(
            @SerializedName("WorldUUIDLeast") long worldUuidLeast,
            @SerializedName("WorldUUIDMost") long worldUuidMost,
            @SerializedName("Dimension") String dimension,
            @SerializedName("Pos") double[] position,
            @SerializedName("Rotation") float[] rotation
    ) {
        public static LocationData of(Location location) {
            return new LocationData(
                    location.getWorld().getUID().getLeastSignificantBits(),
                    location.getWorld().getUID().getMostSignificantBits(),
                    location.getWorld().getKey().toString(),
                    new double[]{location.getX(), location.getY(), location.getZ()},
                    new float[]{location.getYaw(), location.getPitch()}
            );
        }

        public @Nullable Location toLocation() {
            if (position().length != 3 || rotation().length != 2) return null;
            var key = NamespacedKey.fromString(dimension());
            if (key == null) return null;
            var world = Bukkit.getWorld(key);
            if (world == null) return null;
            return new Location(world,
                    position()[0],
                    position()[1],
                    position()[2],
                    rotation()[0],
                    rotation()[1]
            );
        }
    }

    private @Nullable Location getLocation(OfflinePlayer player) {
        var online = player.getPlayer();
        if (online != null) return online.getLocation();
        var file = getNBTFile(player);
        if (file == null) return null;
        return snbt.fromTag(file.getRoot(), LocationData.class).toLocation();
    }

    private @Nullable NBTFile<CompoundTag> getNBTFile(OfflinePlayer player) {
        var worldFolder = Bukkit.getWorlds().get(0).getWorldFolder();
        var io = IO.of(new File(worldFolder, "playerdata"), player.getUniqueId() + ".dat");
        return io.exists() ? new NBTFile<>(io, new CompoundTag()) : null;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length <= 2 ? Arrays.stream(Bukkit.getOfflinePlayers())
                .filter(player -> args.length == 2 || !player.equals(sender))
                .map(OfflinePlayer::getName)
                .filter(Objects::nonNull)
                .filter(name -> args.length != 2 || !args[0].equalsIgnoreCase(name))
                .toList() : null;
    }
}
