package net.thenextlvl.tweaks.command.player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.nbt.NBTInputStream;
import net.thenextlvl.nbt.NBTOutputStream;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.DoubleTag;
import net.thenextlvl.nbt.tag.FloatTag;
import net.thenextlvl.nbt.tag.ListTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.command.suggestion.OfflinePlayerSuggestionProvider;
import org.bukkit.Bukkit;
import org.bukkit.GameRules;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static net.thenextlvl.tweaks.TweaksPlugin.ISSUES;
import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.COMMAND;

@NullMarked
public class OfflineTeleportCommand {
    private final TweaksPlugin plugin;

    public OfflineTeleportCommand(final TweaksPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(final Commands registrar) {
        final var command = Commands.literal(plugin.commands().offlineTeleport.command)
                .requires(stack -> stack.getSender().hasPermission("tweaks.command.offline-tp"))
                .then(Commands.argument("player", StringArgumentType.word())
                        .suggests(new OfflinePlayerSuggestionProvider(plugin))
                        .then(Commands.argument("target", StringArgumentType.word())
                                .suggests(new OfflinePlayerSuggestionProvider(plugin))
                                .executes(context -> {
                                    plugin.getServer().getAsyncScheduler().runNow(plugin,
                                            task -> teleportOther(context));
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .executes(context -> {
                            plugin.getServer().getAsyncScheduler().runNow(plugin,
                                    task -> teleport(context));
                            return Command.SINGLE_SUCCESS;
                        }))
                .build();
        registrar.register(command, "Teleport offline-players to others or you to them",
                plugin.commands().offlineTeleport.aliases);
    }

    private void teleportOther(final CommandContext<CommandSourceStack> context) {
        final var sender = context.getSource().getSender();
        final var player = plugin.getServer().getOfflinePlayer(context.getArgument("player", String.class));
        final var target = plugin.getServer().getOfflinePlayer(context.getArgument("target", String.class));

        final var message = player.equals(target) ? "command.offline.teleport.location" : teleport(player, target);

        plugin.bundle().sendMessage(sender, message,
                Placeholder.parsed("source", String.valueOf(player.getName())),
                Placeholder.parsed("target", String.valueOf(target.getName())));
    }

    private void teleport(final CommandContext<CommandSourceStack> context) {
        if (!(context.getSource().getSender() instanceof final Player sender)) {
            plugin.bundle().sendMessage(context.getSource().getSender(), "command.sender");
            return;
        }

        final var player = plugin.getServer().getOfflinePlayer(context.getArgument("player", String.class));
        final var placeholder = Placeholder.parsed("player", String.valueOf(player.getName()));

        CompletableFuture.<@Nullable Location>completedFuture(getLocation(player))
                .thenCompose(location -> {
                    if (location != null) return sender.teleportAsync(location, COMMAND);
                    return CompletableFuture.completedFuture(false);
                }).thenAccept(success -> {
                    if (Boolean.FALSE.equals(sender.getWorld().getGameRuleValue(GameRules.SEND_COMMAND_FEEDBACK))) return;
                    final var message = success ? "command.offline.teleport.success.to" : "command.offline.teleport.fail.to";
                    plugin.bundle().sendMessage(sender, message, placeholder);
                }).exceptionally(throwable -> {
                    plugin.bundle().sendMessage(sender, "command.offline.teleport.fail.to", placeholder);
                    plugin.getComponentLogger().error("Failed to teleport to offline player", throwable);
                    return null;
                });
    }

    private String teleport(final OfflinePlayer source, final OfflinePlayer target) {
        final var online = source.getPlayer();
        final var location = getLocation(target);
        if (location == null) return "command.offline.teleport.fail";
        if (online == null) return setLocation(source, location)
                ? "command.offline.teleport.success" : "command.offline.teleport.fail";
        if (online.getLocation().equals(location))
            return "command.offline.teleport.location";
        online.teleportAsync(location, COMMAND);
        return "command.offline.teleport.success";
    }

    private boolean setLocation(final OfflinePlayer player, final Location location) {
        final var file = getPlayerDataFile(player).orElse(null);
        if (file == null) return false;
        try (final var input = NBTInputStream.create(file);
             final var output = NBTOutputStream.create(file)) {
            final var tag = input.readNamedTag();
            output.writeTag(tag.getKey(), tag.getValue().toBuilder()
                    .putAll(toTag(location))
                    .build());
            return true;
        } catch (final IOException e) {
            plugin.getComponentLogger().warn("Failed to set location of offline player", e);
            plugin.getComponentLogger().warn("Please look for similar issues or report this on GitHub: {}", ISSUES);
            TweaksPlugin.ERROR_TRACKER.trackError(e);
            return false;
        }
    }

    private @Nullable Location fromTag(final CompoundTag tag) {
        final var world = getWorld(tag);
        if (world == null) return null;
        final var pos = tag.<DoubleTag>getAsList("Pos");
        final var z = pos.get(2).getAsDouble();
        final var y = pos.get(1).getAsDouble();
        final var x = pos.get(0).getAsDouble();
        final var rotation = tag.<FloatTag>getAsList("Rotation");
        final var yaw = rotation.get(0).getAsFloat();
        final var pitch = rotation.get(1).getAsFloat();
        return new Location(world, x, y, z, yaw, pitch);
    }

    private @Nullable World getWorld(final CompoundTag tag) {
        final var uuidLeast = tag.optional("WorldUUIDLeast").map(Tag::getAsLong);
        if (uuidLeast.isEmpty()) return null;

        final var uuidMost = tag.optional("WorldUUIDMost").map(Tag::getAsLong);
        if (uuidMost.isEmpty()) return null;

        final var world = Bukkit.getWorld(new UUID(uuidLeast.get(), uuidMost.get()));
        if (world != null) return world;

        final var dimension = tag.optional("Dimension").map(Tag::getAsString);

        final var key = dimension.map(NamespacedKey::fromString);
        return key.map(Bukkit::getWorld).orElse(null);
    }

    private CompoundTag toTag(final Location location) {
        final var pos = ListTag.builder()
                .add(DoubleTag.of(location.getX()))
                .add(DoubleTag.of(location.getY()))
                .add(DoubleTag.of(location.getZ()))
                .build();

        final var rotation = ListTag.builder()
                .add(FloatTag.of(location.getYaw()))
                .add(FloatTag.of(location.getPitch()))
                .build();

        return CompoundTag.builder()
                .put("WorldUUIDLeast", location.getWorld().getUID().getLeastSignificantBits())
                .put("WorldUUIDMost", location.getWorld().getUID().getMostSignificantBits())
                .put("Dimension", location.getWorld().key().asString())
                .put("Pos", pos)
                .put("Rotation", rotation)
                .build();
    }

    private @Nullable Location getLocation(final OfflinePlayer player) {
        final var online = player.getPlayer();
        if (online != null) return online.getLocation();
        try (final var file = readPlayerData(player)) {
            return file != null ? fromTag(file.readTag()) : null;
        } catch (final IOException e) {
            plugin.getComponentLogger().warn("Failed to get location of offline player", e);
            plugin.getComponentLogger().warn("Please look for similar issues or report this on GitHub: {}", ISSUES);
            TweaksPlugin.ERROR_TRACKER.trackError(e);
            return null;
        }
    }

    private Optional<Path> getPlayerDataFolder() {
        final var overworld = Key.key(Key.MINECRAFT_NAMESPACE, "overworld");
        return Optional.ofNullable(plugin.getServer().getWorld(overworld))
                .map(world -> world.getWorldFolder().toPath().resolve("playerdata"));
    }

    private Optional<Path> getPlayerDataFile(final OfflinePlayer player) {
        return getPlayerDataFolder().map(path -> {
            return path.resolve(player.getUniqueId() + ".dat");
        }).filter(Files::isRegularFile).or(() -> getPlayerDataFolder().map(path -> {
            return path.resolve(player.getUniqueId() + ".dat_old");
        }).filter(Files::isRegularFile));
    }

    private @Nullable NBTInputStream readPlayerData(final OfflinePlayer player) throws IOException {
        final var file = getPlayerDataFile(player).orElse(null);
        return file != null ? NBTInputStream.create(file) : null;
    }
}
