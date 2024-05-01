package net.thenextlvl.tweaks.command.player;

import core.io.IO;
import core.nbt.file.NBTFile;
import core.nbt.tag.CompoundTag;
import core.nbt.tag.DoubleTag;
import core.nbt.tag.FloatTag;
import core.nbt.tag.ListTag;
import core.nbt.tag.Tag;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.api.CommandInfo;
import net.thenextlvl.tweaks.command.api.CommandSenderException;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
        }, "tpo-thread").start();
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
        file.getRoot().addAll(toTag(location));
        file.save();
        return true;
    }

    private Location fromTag(CompoundTag tag) {
        var world = getWorld(tag);
        var pos = tag.<DoubleTag>getAsList("Pos");
        var z = pos.get(2).getAsDouble();
        var y = pos.get(1).getAsDouble();
        var x = pos.get(0).getAsDouble();
        var rotation = tag.<FloatTag>getAsList("Rotation");
        var yaw = rotation.get(0).getAsFloat();
        var pitch = rotation.get(1).getAsFloat();
        return new Location(world, x, y, z, yaw, pitch);
    }

    private @Nullable World getWorld(CompoundTag tag) {
        var uuidLeast = tag.optional("WorldUUIDLeast").map(Tag::getAsLong);
        if (uuidLeast.isEmpty()) return null;

        var uuidMost = tag.optional("WorldUUIDMost").map(Tag::getAsLong);
        if (uuidMost.isEmpty()) return null;

        var world = Bukkit.getWorld(new UUID(uuidLeast.get(), uuidMost.get()));
        if (world != null) return world;

        var dimension = tag.optional("Dimension").map(Tag::getAsString);

        var key = dimension.map(NamespacedKey::fromString);
        return key.map(Bukkit::getWorld).orElse(null);
    }

    private CompoundTag toTag(Location location) {
        var pos = new ListTag<>(DoubleTag.ID);
        pos.add(new DoubleTag(location.getX()));
        pos.add(new DoubleTag(location.getY()));
        pos.add(new DoubleTag(location.getZ()));

        var rotation = new ListTag<>(FloatTag.ID);
        rotation.add(new FloatTag(location.getYaw()));
        rotation.add(new FloatTag(location.getPitch()));

        var tag = new CompoundTag();
        tag.add("WorldUUIDLeast", location.getWorld().getUID().getLeastSignificantBits());
        tag.add("WorldUUIDMost", location.getWorld().getUID().getMostSignificantBits());
        tag.add("Dimension", location.getWorld().getKey().toString());
        tag.add("Pos", pos);
        tag.add("Rotation", rotation);
        return tag;
    }

    private @Nullable Location getLocation(OfflinePlayer player) {
        var online = player.getPlayer();
        if (online != null) return online.getLocation();
        var file = getNBTFile(player);
        return file != null ? fromTag(file.getRoot()) : null;
    }

    private @Nullable NBTFile<CompoundTag> getNBTFile(OfflinePlayer player) {
        var data = new File(Bukkit.getWorlds().getFirst().getWorldFolder(), "playerdata");
        var io = IO.of(data, player.getUniqueId() + ".dat");
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
