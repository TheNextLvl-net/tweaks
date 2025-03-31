package net.thenextlvl.tweaks.controller;

import net.kyori.adventure.key.Key;
import net.thenextlvl.tweaks.TweaksPlugin;
import net.thenextlvl.tweaks.model.NamedLocation;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@NullMarked
public class DataController {
    private static final String TPA_TOGGLED = "tpa_toggled";
    private static final String MSG_TOGGLED = "msg_toggled";
    private final Connection connection;
    private final TweaksPlugin plugin;

    public DataController(TweaksPlugin plugin) {
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + new File(plugin.getDataFolder(), "saves.db"));
            this.plugin = plugin;
            createHomesTable();
            createSettingsTable();
            createWarpsTable();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    public Optional<Location> getHome(OfflinePlayer player, String name) {
        try {
            return Optional.ofNullable(executeQuery("""
                            SELECT world, x, y, z, yaw, pitch FROM homes
                            WHERE uuid = ? AND name = ?
                            """,
                    resultSet -> resultSet.next() ? parseLocation(resultSet) : null,
                    player.getUniqueId(), name));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get home location", e);
        }
    }

    public boolean hasHome(OfflinePlayer player, String name) {
        try {
            return Boolean.TRUE.equals(executeQuery(
                    "SELECT COUNT(*) FROM homes WHERE uuid = ? AND name = ?",
                    resultSet -> resultSet.next() && resultSet.getInt(1) > 0,
                    player.getUniqueId(), name
            ));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check if home exists", e);
        }
    }

    public int getHomeCount(OfflinePlayer player) {
        try {
            return Objects.requireNonNull(executeQuery(
                    "SELECT COUNT(*) FROM homes WHERE uuid = ?",
                    resultSet -> resultSet.next() ? resultSet.getInt(1) : -1,
                    player.getUniqueId()
            ));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get home count", e);
        }
    }

    public Set<NamedLocation> getHomes(OfflinePlayer player) {
        try {
            return Objects.requireNonNullElseGet(executeQuery(
                    "SELECT name, world, x, y, z, yaw, pitch FROM homes WHERE uuid = ?",
                    this::parseNamedLocations, player.getUniqueId()
            ), Set::of);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get homes", e);
        }
    }

    public boolean deleteHome(OfflinePlayer player, String name) {
        try {
            return executeUpdate("DELETE FROM homes WHERE uuid = ? AND name = ?", player.getUniqueId(), name) != 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete home", e);
        }
    }

    public void setHome(OfflinePlayer player, String name, Location location) {
        try {
            executeUpdate("DELETE FROM homes WHERE uuid = ? AND name = ?",
                    player.getUniqueId(), name);
            executeUpdate("INSERT INTO homes (uuid, name, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    player.getUniqueId(), name,
                    location.getWorld().key().asString(),
                    location.getX(),
                    location.getY(),
                    location.getZ(),
                    location.getYaw(),
                    location.getPitch());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to set home", e);
        }
    }

    /**
     * Checks if the specified player has TPA (Teleport Request) toggled on.
     * When toggled on, the player does not accept TPA requests.
     *
     * @param player the player whose TPA toggle status is being checked
     * @return true if TPA requests are blocked, false if TPA requests are accepted
     */
    public boolean isTpaToggled(OfflinePlayer player) {
        return isSettingToggled(player, TPA_TOGGLED);
    }

    /**
     * Sets the TPA (Teleport Request) toggle setting for the specified player.
     * When toggled to true, the player does not accept TPA requests. When false, the player accepts TPA requests.
     *
     * @param player  the player for whom the TPA toggle setting should be set
     * @param toggled true to block TPA requests, false to accept TPA requests
     * @return true if the operation changed the player's TPA toggle setting, false otherwise
     */
    public boolean setTpaToggled(OfflinePlayer player, boolean toggled) {
        return setSettingToggled(player, toggled, TPA_TOGGLED);
    }

    /**
     * Toggles the TPA (Teleport Request) setting for the given player.
     * If the player currently accepts TPA requests, they will start blocking them, and vice versa.
     *
     * @param player the player whose TPA setting is to be toggled
     * @return the new TPA setting; false if the player now accepts TPA requests, true if the player blocks them
     */
    public boolean toggleTpa(OfflinePlayer player) {
        return toggleSetting(player, TPA_TOGGLED);
    }

    /**
     * Checks if the message toggle setting is enabled for the specified player.
     *
     * @param player the player whose message toggle status is being checked
     * @return true if the player has the message toggle enabled, false otherwise
     */
    public boolean isMsgToggled(OfflinePlayer player) {
        return isSettingToggled(player, MSG_TOGGLED);
    }

    /**
     * Sets the message toggle setting for the specified player. When set to true, the player does not accept
     * private messages. When set to false, the player accepts private messages.
     *
     * @param player  the player for whom the message toggle setting should be set
     * @param toggled true to block private messages, false to accept private messages
     * @return true if the operation changed the player's message toggle setting, false otherwise
     */
    public boolean setMsgToggled(OfflinePlayer player, boolean toggled) {
        return setSettingToggled(player, toggled, MSG_TOGGLED);
    }

    /**
     * Toggles the message setting for the given player.
     * If the player currently accepts private messages, they will start blocking them, and vice versa.
     *
     * @param player the player whose message setting is to be toggled
     * @return the new message setting; false if the player now accepts private messages, true if the player blocks them
     */
    public boolean toggleMsg(OfflinePlayer player) {
        return toggleSetting(player, MSG_TOGGLED);
    }

    private boolean isSettingToggled(OfflinePlayer player, String setting) {
        try {
            return Boolean.TRUE.equals(executeQuery(
                    "SELECT COUNT(*) FROM settings WHERE uuid = ? AND setting = ?",
                    resultSet -> resultSet.next() && resultSet.getInt(1) > 0,
                    player.getUniqueId(), setting
            ));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check if setting is toggled", e);
        }
    }

    private boolean setSettingToggled(OfflinePlayer player, boolean toggled, String setting) {
        try {
            return toggled ? executeUpdate(
                    "INSERT OR IGNORE INTO settings (uuid, setting) VALUES (?, ?)",
                    player.getUniqueId(), setting
            ) != 0 : removeSetting(player, setting);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to set setting toggled", e);
        }
    }

    private boolean removeSetting(OfflinePlayer player, String setting) {
        try {
            return executeUpdate(
                    "DELETE FROM settings WHERE uuid = ? AND setting = ?",
                    player.getUniqueId(), setting
            ) != 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to remove setting", e);
        }
    }

    private boolean toggleSetting(OfflinePlayer player, String setting) {
        var toggled = isSettingToggled(player, setting);
        return setSettingToggled(player, !toggled, setting) != toggled;
    }

    public Optional<Location> getWarp(String name) {
        try {
            return Optional.ofNullable(executeQuery("SELECT world, x, y, z, yaw, pitch FROM warps WHERE name = ?", resultSet -> {
                if (!resultSet.next()) return null;
                return parseLocation(resultSet);
            }, name));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get warp location", e);
        }
    }

    public Set<NamedLocation> getWarps() {
        try {
            return Objects.requireNonNullElseGet(executeQuery(
                    "SELECT name, world, x, y, z, yaw, pitch FROM warps",
                    this::parseNamedLocations
            ), Set::of);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get warps", e);
        }
    }

    public boolean deleteWarp(String name) {
        try {
            return executeUpdate("DELETE FROM warps WHERE name = ?", name) != 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete warp", e);
        }
    }

    public void setWarp(String name, Location location) {
        try {
            executeUpdate("""
                            INSERT INTO warps (name, world, x, y, z, yaw, pitch)
                            VALUES (?, ?, ?, ?, ?, ?, ?)
                            ON CONFLICT(name) DO UPDATE SET
                             world = excluded.world,
                             x = excluded.x,
                             y = excluded.y,
                             z = excluded.z,
                             yaw = excluded.yaw,
                             pitch = excluded.pitch
                            """,
                    name,
                    location.getWorld().key().asString(),
                    location.getX(),
                    location.getY(),
                    location.getZ(),
                    location.getYaw(),
                    location.getPitch());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to set warp", e);
        }
    }

    private Set<NamedLocation> parseNamedLocations(ResultSet resultSet) throws SQLException {
        var homes = new HashSet<NamedLocation>();
        while (resultSet.next()) {
            var location = parseLocation(resultSet);
            if (location == null) continue;
            homes.add(new NamedLocation(resultSet.getString("name"), location));
        }
        return homes;
    }

    @SuppressWarnings("PatternValidation")
    private @Nullable Location parseLocation(ResultSet resultSet) throws SQLException {
        var key = Key.key(resultSet.getString("world"));
        var world = plugin.getServer().getWorld(key);
        if (world == null) return null;
        var x = resultSet.getDouble("x");
        var y = resultSet.getDouble("y");
        var z = resultSet.getDouble("z");
        var yaw = resultSet.getFloat("yaw");
        var pitch = resultSet.getFloat("pitch");
        return new Location(world, x, y, z, yaw, pitch);
    }

    protected void createHomesTable() throws SQLException {
        executeUpdate("""
                CREATE TABLE IF NOT EXISTS homes (
                  uuid TEXT NOT NULL,
                  name TEXT NOT NULL,
                  world TEXT NOT NULL,
                  x DOUBLE NOT NULL,
                  y DOUBLE NOT NULL,
                  z DOUBLE NOT NULL,
                  yaw FLOAT NOT NULL,
                  pitch FLOAT NOT NULL,
                  UNIQUE (uuid, name)
                )""");
    }

    private void createSettingsTable() throws SQLException {
        executeUpdate("""
                CREATE TABLE IF NOT EXISTS settings (
                  uuid TEXT NOT NULL,
                  setting TEXT NOT NULL,
                  UNIQUE (uuid, setting)
                )""");
    }

    private void createWarpsTable() throws SQLException {
        executeUpdate("""
                CREATE TABLE IF NOT EXISTS warps (
                  name TEXT NOT NULL UNIQUE PRIMARY KEY,
                  world TEXT NOT NULL,
                  x DOUBLE NOT NULL,
                  y DOUBLE NOT NULL,
                  z DOUBLE NOT NULL,
                  yaw FLOAT NOT NULL,
                  pitch FLOAT NOT NULL
                )""");
    }

    @SuppressWarnings("SqlSourceToSinkFlow")
    protected <T> @Nullable T executeQuery(String query, ThrowingFunction<ResultSet, T> mapper, @Nullable Object... parameters) throws SQLException {
        try (var preparedStatement = connection.prepareStatement(query)) {
            for (var i = 0; i < parameters.length; i++)
                preparedStatement.setObject(i + 1, parameters[i]);
            try (var resultSet = preparedStatement.executeQuery()) {
                return ThrowingFunction.unchecked(mapper).apply(resultSet);
            }
        }
    }

    @SuppressWarnings("SqlSourceToSinkFlow")
    protected int executeUpdate(String query, @Nullable Object... parameters) throws SQLException {
        try (var preparedStatement = connection.prepareStatement(query)) {
            for (var i = 0; i < parameters.length; i++)
                preparedStatement.setObject(i + 1, parameters[i]);
            return preparedStatement.executeUpdate();
        }
    }

    @FunctionalInterface
    protected interface ThrowingFunction<T, R> {
        @Nullable
        R apply(T t) throws SQLException;

        static <T, R> ThrowingFunction<T, R> unchecked(ThrowingFunction<T, R> f) {
            return f;
        }
    }
}
