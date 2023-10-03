package net.thenextlvl.tweaks.command.player;

import core.nbt.file.NBTFile;
import core.nbt.tag.*;
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
        var nbt = file.getRoot();
        nbt.remove("WorldUUIDLeast");
        nbt.remove("WorldUUIDMost");
        nbt.remove("Dimension");
        nbt.remove("Rotation");
        nbt.remove("Pos");
        nbt.add(new LongTag("WorldUUIDLeast", location.getWorld().getUID().getLeastSignificantBits()));
        nbt.add(new LongTag("WorldUUIDMost", location.getWorld().getUID().getMostSignificantBits()));
        nbt.add(new StringTag("Dimension", location.getWorld().getKey().toString()));
        nbt.add(new ListTag<>("Pos", List.of(
                new DoubleTag(location.getX()),
                new DoubleTag(location.getY()),
                new DoubleTag(location.getZ())
        ), DoubleTag.ID));
        nbt.add(new ListTag<>("Rotation", List.of(
                new FloatTag(location.getYaw()),
                new FloatTag(location.getPitch())
        ), FloatTag.ID));
        file.save();
        return true;
    }

    private @Nullable Location getLocation(OfflinePlayer player) {
        var online = player.getPlayer();
        if (online != null) return online.getLocation();
        var file = getNBTFile(player);
        if (file == null) return null;
        var nbt = file.getRoot();
        if (!nbt.containsKey("Dimension") || !nbt.get("Dimension").isString()) return null;
        if (!nbt.containsKey("Pos") || !nbt.get("Pos").isList()) return null;
        if (!nbt.containsKey("Rotation") || !nbt.get("Rotation").isList()) return null;
        var position = nbt.<DoubleTag>getAsList("Pos");
        var rotation = nbt.<FloatTag>getAsList("Rotation");
        if (position.size() != 3 || rotation.size() != 2) return null;
        var dimension = nbt.get("Dimension").getAsString();
        var key = NamespacedKey.fromString(dimension);
        System.out.println(key);
        if (key == null) return null;
        var world = Bukkit.getWorld(key);
        if (world == null) return null;
        return new Location(world,
                position.get(0).getValue(),
                position.get(1).getValue(),
                position.get(2).getValue(),
                rotation.get(0).getValue(),
                rotation.get(1).getValue()
        );
    }

    private @Nullable NBTFile<CompoundTag> getNBTFile(OfflinePlayer player) {
        var worldFolder = Bukkit.getWorlds().get(0).getWorldFolder();
        var file = new File(new File(worldFolder, "playerdata"), player.getUniqueId() + ".dat");
        return file.exists() ? new NBTFile<>(file, new CompoundTag()) : null;
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
