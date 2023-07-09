package net.thenextlvl.tweaks.util;

import core.api.file.format.MessageFile;
import core.api.placeholder.MessageKey;
import core.api.placeholder.SystemMessageKey;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

public class Messages {
    public static final Locale ENGLISH = Locale.forLanguageTag("en-US");
    private static final TweaksPlugin plugin = JavaPlugin.getPlugin(TweaksPlugin.class);
    public static final SystemMessageKey<CommandSender> PREFIX = new SystemMessageKey<>("tweaks.prefix", plugin.formatter());
    public static final SystemMessageKey<CommandSender> COMMAND_USAGE = new SystemMessageKey<>("command.usage", plugin.formatter());
    public static final MessageKey<CommandSender> COMMAND_PERMISSION = new MessageKey<>("command.permission", plugin.formatter());
    public static final MessageKey<CommandSender> COMMAND_SENDER = new MessageKey<>("command.sender", plugin.formatter());
    public static final MessageKey<CommandSender> PLAYER_NOT_ONLINE = new MessageKey<>("player.not.online", plugin.formatter());
    public static final MessageKey<CommandSender> PLAYER_NOT_FOUND = new MessageKey<>("player.not.found", plugin.formatter());
    public static final MessageKey<CommandSender> PLAYER_NOT_AFFECTED = new MessageKey<>("player.not.affected", plugin.formatter());
    public static final MessageKey<CommandSender> WORLD_NOT_AFFECTED = new MessageKey<>("world.not.affected", plugin.formatter());
    public static final MessageKey<CommandSender> WORLD_NOT_FOUND = new MessageKey<>("world.not.found", plugin.formatter());
    public static final MessageKey<CommandSender> SATISFIED_HUNGER_SELF = new MessageKey<>("hunger.satisfied.self", plugin.formatter());
    public static final MessageKey<CommandSender> SATISFIED_HUNGER_OTHERS = new MessageKey<>("hunger.satisfied.others", plugin.formatter());
    public static final MessageKey<CommandSender> RESTORED_HEALTH_SELF = new MessageKey<>("health.restored.self", plugin.formatter());
    public static final MessageKey<CommandSender> RESTORED_HEALTH_OTHERS = new MessageKey<>("health.restored.others", plugin.formatter());
    public static final MessageKey<CommandSender> ENABLED_FLIGHT_SELF = new MessageKey<>("flight.enabled.self", plugin.formatter());
    public static final MessageKey<CommandSender> ENABLED_FLIGHT_OTHERS = new MessageKey<>("flight.enabled.others", plugin.formatter());
    public static final MessageKey<CommandSender> DISABLED_FLIGHT_SELF = new MessageKey<>("flight.disabled.self", plugin.formatter());
    public static final MessageKey<CommandSender> DISABLED_FLIGHT_OTHERS = new MessageKey<>("flight.disabled.others", plugin.formatter());
    public static final MessageKey<CommandSender> PING_SELF = new MessageKey<>("ping.self", plugin.formatter());
    public static final MessageKey<CommandSender> PING_OTHERS = new MessageKey<>("ping.others", plugin.formatter());
    public static final MessageKey<CommandSender> HAT_EQUIPPED = new MessageKey<>("hat.equipped", plugin.formatter());
    public static final MessageKey<CommandSender> HOLD_ITEM = new MessageKey<>("hold.item", plugin.formatter());
    public static final MessageKey<CommandSender> LAST_SEEN_NOW = new MessageKey<>("last.seen.now", plugin.formatter());
    public static final MessageKey<CommandSender> LAST_SEEN_TIME = new MessageKey<>("last.seen.time", plugin.formatter());
    public static final MessageKey<CommandSender> BACK_EMPTY = new MessageKey<>("back.empty", plugin.formatter());
    public static final MessageKey<CommandSender> BACK_TELEPORT_SUCCESS = new MessageKey<>("back.teleport.success", plugin.formatter());
    public static final MessageKey<CommandSender> BACK_TELEPORT_FAIL = new MessageKey<>("back.teleport.fail", plugin.formatter());
    public static final MessageKey<CommandSender> TIME_DAY = new MessageKey<>("time.day", plugin.formatter());
    public static final MessageKey<CommandSender> TIME_NIGHT = new MessageKey<>("time.night", plugin.formatter());
    public static final MessageKey<CommandSender> WEATHER_RAIN = new MessageKey<>("weather.rain", plugin.formatter());
    public static final MessageKey<CommandSender> WEATHER_SUN = new MessageKey<>("weather.sun", plugin.formatter());
    public static final MessageKey<CommandSender> WEATHER_THUNDER = new MessageKey<>("weather.thunder", plugin.formatter());
    public static final MessageKey<CommandSender> LORE_EDIT_TIP = new MessageKey<>("item.lore.tip", plugin.formatter());
    public static final MessageKey<CommandSender> LORE_EDIT_SUCCESS = new MessageKey<>("item.lore.success", plugin.formatter());
    public static final MessageKey<CommandSender> LORE_EDIT_FAIL = new MessageKey<>("item.lore.fail", plugin.formatter());
    public static final MessageKey<CommandSender> INVENTORY_FULL = new MessageKey<>("inventory.full", plugin.formatter());
    public static final MessageKey<CommandSender> ITEM_RECEIVED = new MessageKey<>("item.received", plugin.formatter());
    public static final MessageKey<CommandSender> INVALID_ITEM = new MessageKey<>("item.invalid", plugin.formatter());
    public static final MessageKey<CommandSender> INVALID_ENCHANTMENT = new MessageKey<>("enchantment.invalid", plugin.formatter());
    public static final MessageKey<CommandSender> ENCHANTMENT_NOT_APPLICABLE = new MessageKey<>("enchantment.not.applicable", plugin.formatter());
    public static final MessageKey<CommandSender> ENCHANTED_ITEM = new MessageKey<>("enchantment.applied", plugin.formatter());
    public static final MessageKey<CommandSender> GAMEMODE_CHANGED_SELF = new MessageKey<>("gamemode.changed.self", plugin.formatter());
    public static final MessageKey<CommandSender> GAMEMODE_CHANGED_OTHERS = new MessageKey<>("gamemode.changed.others", plugin.formatter());
    public static final MessageKey<CommandSender> WALK_SPEED_CHANGED_SELF = new MessageKey<>("speed.walk.changed.self", plugin.formatter());
    public static final MessageKey<CommandSender> WALK_SPEED_CHANGED_OTHERS = new MessageKey<>("speed.walk.changed.others", plugin.formatter());
    public static final MessageKey<CommandSender> FLY_SPEED_CHANGED_SELF = new MessageKey<>("speed.fly.changed.self", plugin.formatter());
    public static final MessageKey<CommandSender> FLY_SPEED_CHANGED_OTHERS = new MessageKey<>("speed.fly.changed.others", plugin.formatter());
    public static final MessageKey<CommandSender> REPAIRED_ALL = new MessageKey<>("item.repaired.all", plugin.formatter());
    public static final MessageKey<CommandSender> REPAIRED_ITEM = new MessageKey<>("item.repaired", plugin.formatter());
    public static final MessageKey<CommandSender> ITEM_RENAME_FAIL = new MessageKey<>("item.rename.fail", plugin.formatter());
    public static final MessageKey<CommandSender> ITEM_RENAME_SUCCESS = new MessageKey<>("item.rename.success", plugin.formatter());
    public static final MessageKey<CommandSender> ITEM_HEAD_VALUE = new MessageKey<>("item.head.value", plugin.formatter());
    public static final MessageKey<CommandSender> ITEM_HEAD_PLAYER = new MessageKey<>("item.head.player", plugin.formatter());
    public static final MessageKey<CommandSender> ITEM_HEAD_URL = new MessageKey<>("item.head.url", plugin.formatter());
    public static final MessageKey<CommandSender> ITEM_HEAD_NONE = new MessageKey<>("item.head.none", plugin.formatter());
    public static final MessageKey<CommandSender> ITEM_HEAD_RECEIVED = new MessageKey<>("item.head.received", plugin.formatter());
    public static final MessageKey<CommandSender> GOD_MODE_ACTIVE_SELF = new MessageKey<>("god.active.self", plugin.formatter());
    public static final MessageKey<CommandSender> GOD_MODE_ACTIVE_OTHERS = new MessageKey<>("god.active.others", plugin.formatter());
    public static final MessageKey<CommandSender> GOD_MODE_INACTIVE_SELF = new MessageKey<>("god.inactive.self", plugin.formatter());
    public static final MessageKey<CommandSender> GOD_MODE_INACTIVE_OTHERS = new MessageKey<>("god.inactive.others", plugin.formatter());

    static {
        initEnglish();
        initGerman();
        initRoot();
    }

    private static void initEnglish() {
        var file = MessageFile.getOrCreate(ENGLISH);
        file.setDefault(COMMAND_PERMISSION, "%prefix% <red>You have no rights <dark_gray>(<dark_red>%permission%<dark_gray>)");
        file.setDefault(COMMAND_SENDER, "%prefix% <red>You cannot use this command");
        file.setDefault(PLAYER_NOT_ONLINE, "%prefix% <red>The player <dark_red>%player%<red> is not online");
        file.setDefault(PLAYER_NOT_FOUND, "%prefix% <red>The player <dark_red>%player%<red> is unknown to us");
        file.setDefault(PLAYER_NOT_AFFECTED, "%prefix% <red>The player <dark_red>%player%<red> is not affected");
        file.setDefault(WORLD_NOT_AFFECTED, "%prefix% <red>The world <dark_red>%world%<red> is not affected");
        file.setDefault(WORLD_NOT_FOUND, "%prefix% <red>The world <dark_red>%world%<red> does not exist");
        file.setDefault(SATISFIED_HUNGER_SELF, "%prefix% <white>Your hunger has been satisfied");
        file.setDefault(SATISFIED_HUNGER_OTHERS, "%prefix% <green>%player%'s<white> hunger has been satisfied");
        file.setDefault(RESTORED_HEALTH_SELF, "%prefix% <white>Your health has been restored");
        file.setDefault(RESTORED_HEALTH_OTHERS, "%prefix% <green>%player%'s<white> health has been restored");
        file.setDefault(DISABLED_FLIGHT_OTHERS, "%prefix% <green>%player%<white> can no longer fly");
        file.setDefault(ENABLED_FLIGHT_OTHERS, "%prefix% <green>%player%'s<white> is now able to fly");
        file.setDefault(DISABLED_FLIGHT_SELF, "%prefix% <white>You can no longer fly");
        file.setDefault(ENABLED_FLIGHT_SELF, "%prefix% <white>You are now able to fly");
        file.setDefault(PING_SELF, "%prefix% <white>Your connection latency is around <green>%ping%ms");
        file.setDefault(PING_OTHERS, "%prefix% <green>%player%'s<white> connection latency is around <green>%ping%ms");
        file.setDefault(HAT_EQUIPPED, "%prefix% <white>You have equipped your item as a hat");
        file.setDefault(HOLD_ITEM, "%prefix% <red>You have to hold an item in your main hand");
        file.setDefault(LAST_SEEN_NOW, "%prefix% <green>%player%<white> is online at the moment");
        file.setDefault(LAST_SEEN_TIME, "%prefix% <green>%player%<white> was last seen <green>%time%");
        file.setDefault(BACK_EMPTY, "%prefix% <red>You can't go back any further");
        file.setDefault(BACK_TELEPORT_SUCCESS, "%prefix% <white>Teleported you to your last position");
        file.setDefault(BACK_TELEPORT_FAIL, "%prefix% <red>Failed to teleport you to your last position");
        file.setDefault(TIME_DAY, "%prefix% <white>It is now day in <green>%world%");
        file.setDefault(TIME_NIGHT, "%prefix% <white>It is now night in <green>%world%");
        file.setDefault(WEATHER_RAIN, "%prefix% <white>It is now raining in <green>%world%");
        file.setDefault(WEATHER_SUN, "%prefix% <white>It is now sunny in <green>%world%");
        file.setDefault(WEATHER_THUNDER, "%prefix% <white>It is now thundering in <green>%world%");
        file.setDefault(LORE_EDIT_TIP, "%prefix% <red>Use <dark_red>\\n<red> to add a new line and <dark_red>\\t<red> to add indentations");
        file.setDefault(LORE_EDIT_SUCCESS, "%prefix% <white>Successfully changed the lore of your item");
        file.setDefault(LORE_EDIT_FAIL, "%prefix% <red>Failed to change the lore of your item");
        file.setDefault(INVENTORY_FULL, "%prefix% <red>Your inventory is full");
        file.setDefault(ITEM_RECEIVED, "%prefix% <white>You received <green>%amount% x <item>");
        file.setDefault(INVALID_ITEM, "%prefix% <dark_red>%item%<red> is not a valid item");
        file.setDefault(INVALID_ENCHANTMENT, "%prefix% <dark_red>%enchantment%<red> is not a valid enchantment");
        file.setDefault(ENCHANTMENT_NOT_APPLICABLE, "%prefix% <red>This enchantment is not applicable on <dark_red><item>");
        file.setDefault(ENCHANTED_ITEM, "%prefix% <white>Applied <green><enchantment><white> to your item");
        file.setDefault(GAMEMODE_CHANGED_SELF, "%prefix% <white>You are now in <green><gamemode>");
        file.setDefault(GAMEMODE_CHANGED_OTHERS, "%prefix% <green>%player% <white>is now in <green><gamemode>");
        file.setDefault(WALK_SPEED_CHANGED_SELF, "%prefix% <white>Your walk speed is now <green>%speed%");
        file.setDefault(WALK_SPEED_CHANGED_OTHERS, "%prefix% <green>%player%'s <white>walk speed is now <green>%speed%");
        file.setDefault(FLY_SPEED_CHANGED_SELF, "%prefix% <white>Your fly speed is now <green>%speed%");
        file.setDefault(FLY_SPEED_CHANGED_OTHERS, "%prefix% <green>%player%'s <white>fly speed is now <green>%speed%");
        file.setDefault(REPAIRED_ALL, "%prefix% <white>All of your items got repaired");
        file.setDefault(REPAIRED_ITEM, "%prefix% <white>Your item got repaired");
        file.setDefault(ITEM_RENAME_FAIL, "%prefix% <red>Failed to rename your item");
        file.setDefault(ITEM_RENAME_SUCCESS, "%prefix% <white>Successfully renamed your item");
        file.setDefault(ITEM_HEAD_VALUE, "%prefix% <gray>Skull value<dark_gray>: " +
                "<click:copy_to_clipboard:\"%full-value%\"><hover:show_text:\"Click to Copy to Clipboard\"><white>%value%");
        file.setDefault(ITEM_HEAD_PLAYER, "%prefix% <gray>Skull owner<dark_gray>: <white>%owner%");
        file.setDefault(ITEM_HEAD_URL, "%prefix% <gray>Skull url<dark_gray>: " +
                "<click:copy_to_clipboard:\"%full-url%\"><hover:show_text:\"Click to Copy to Clipboard\"><white>%url%");
        file.setDefault(ITEM_HEAD_NONE, "%prefix% <red>Could not find matching skull data");
        file.setDefault(ITEM_HEAD_RECEIVED, "%prefix% <white>You received a player head");
        file.setDefault(GOD_MODE_ACTIVE_SELF, "%prefix% <white>You are now invulnerable");
        file.setDefault(GOD_MODE_ACTIVE_OTHERS, "%prefix% <green>%player% <white>is now invulnerable");
        file.setDefault(GOD_MODE_INACTIVE_SELF, "%prefix% <white>You are no longer invulnerable");
        file.setDefault(GOD_MODE_INACTIVE_OTHERS, "%prefix% <green>%player% <white>is no longer invulnerable");
        file.save();
    }

    private static void initGerman() {
        var file = MessageFile.getOrCreate(Locale.forLanguageTag("de-DE"));
        file.setDefault(COMMAND_PERMISSION, "%prefix% <red>Darauf hast du keine rechte <dark_gray>(<dark_red>%permission%<dark_gray>)");
        file.setDefault(COMMAND_SENDER, "%prefix% <red>Du kannst diesen command nicht nutzen");
        file.setDefault(PLAYER_NOT_ONLINE, "%prefix% <red>Der Spieler <dark_red>%player%<red> ist nicht online");
        file.setDefault(PLAYER_NOT_FOUND, "%prefix% <red>Der Spieler <dark_red>%player%<red> ist uns nicht bekannt");
        file.setDefault(PLAYER_NOT_AFFECTED, "%prefix% <red>Der Spieler <dark_red>%player%<red> ist nicht betroffen");
        file.setDefault(WORLD_NOT_AFFECTED, "%prefix% <red>Die Welt <dark_red>%world%<red> ist nicht betroffen");
        file.setDefault(WORLD_NOT_FOUND, "%prefix% <red>Die Welt <dark_red>%world%<red> existiert nicht");
        file.setDefault(SATISFIED_HUNGER_SELF, "%prefix% <white>Dein Hunger wurde gestillt");
        file.setDefault(SATISFIED_HUNGER_OTHERS, "%prefix% <green>%player%'s<white> Hunger wurde gestillt");
        file.setDefault(RESTORED_HEALTH_SELF, "%prefix% <white>Du wurdest vollständig geheilt");
        file.setDefault(RESTORED_HEALTH_OTHERS, "%prefix% <green>%player%<white> wurde vollständig geheilt");
        file.setDefault(DISABLED_FLIGHT_OTHERS, "%prefix% <green>%player%<white> kann nicht länger fliegen");
        file.setDefault(ENABLED_FLIGHT_OTHERS, "%prefix% <green>%player%'s<white> kann ab jetzt fliegen");
        file.setDefault(DISABLED_FLIGHT_SELF, "%prefix% <white>Du kannst nicht länger fliegen");
        file.setDefault(ENABLED_FLIGHT_SELF, "%prefix% <white>Du kannst ab jetzt fliegen");
        file.setDefault(PING_SELF, "%prefix% <white>Du hast einen verbindungslatenz von <green>%ping%ms");
        file.setDefault(PING_OTHERS, "%prefix% <green>%player%<white> hat eine verbindungslatenz von <green>%ping%ms");
        file.setDefault(HOLD_ITEM, "%prefix% <red>Du musst ein Item in der Haupthand halten");
        file.setDefault(LAST_SEEN_NOW, "%prefix% <green>%player%<white> is aktuell online");
        file.setDefault(LAST_SEEN_TIME, "%prefix% <green>%player%<white> war zuletzt online <green>%time%");
        file.setDefault(BACK_EMPTY, "%prefix% <red>Du kannst nicht weiter zurück gehen");
        file.setDefault(BACK_TELEPORT_SUCCESS, "%prefix% <white>Du wurdest zu deiner letzten Position teleportiert");
        file.setDefault(BACK_TELEPORT_FAIL, "%prefix% <red>Du konntest nicht zu deiner letzten Position teleportiert werden");
        file.setDefault(TIME_DAY, "%prefix% <white>Es ist jetzt tag in <green>%world%");
        file.setDefault(TIME_NIGHT, "%prefix% <white>Es ist jetzt nacht in <green>%world%");
        file.setDefault(WEATHER_RAIN, "%prefix% <white>Es regnet jetzt in <green>%world%");
        file.setDefault(WEATHER_SUN, "%prefix% <white>Es scheint jetzt die sonne in <green>%world%");
        file.setDefault(WEATHER_THUNDER, "%prefix% <white>Es gewittert jetzt in <green>%world%");
        file.setDefault(LORE_EDIT_TIP, "%prefix% <red>Nutze <dark_red>\\n<red> für neue Zeile und <dark_red>\\t<red> für Einrückungen");
        file.setDefault(LORE_EDIT_SUCCESS, "%prefix% <white>Die Beschreibung deines Items wurde erfolgreich angepasst");
        file.setDefault(LORE_EDIT_FAIL, "%prefix% <red>Die Beschreibung deines Items konnte nicht angepasst werden");
        file.setDefault(INVENTORY_FULL, "%prefix% <red>Dein Inventar ist voll");
        file.setDefault(ITEM_RECEIVED, "%prefix% <white>Du hast <green>%amount% x <item><white> erhalten");
        file.setDefault(INVALID_ITEM, "%prefix% <dark_red>%item%<red> ist kein gültiges Item");
        file.setDefault(INVALID_ENCHANTMENT, "%prefix% <dark_red>%enchantment%<red> ist keine gültige Verzauberung");
        file.setDefault(ENCHANTMENT_NOT_APPLICABLE, "%prefix% <red>Dieses enchantment ist nicht auf <dark_red><item><red> anwendbar");
        file.setDefault(ENCHANTED_ITEM, "%prefix% <white>Dein Item wurde mit <green><enchantment><white> verzaubert");
        file.setDefault(GAMEMODE_CHANGED_SELF, "%prefix% <white>Du bist jetzt im <green><gamemode>");
        file.setDefault(GAMEMODE_CHANGED_OTHERS, "%prefix% <green>%player% <white>ist jetzt im <green><gamemode>");
        file.setDefault(WALK_SPEED_CHANGED_SELF, "%prefix% <white>Deine Laufgeschwindigkeit is jetzt <green>%speed%");
        file.setDefault(WALK_SPEED_CHANGED_OTHERS, "%prefix% <green>%player%'s <white>Laufgeschwindigkeit ist jetzt <green>%speed%");
        file.setDefault(FLY_SPEED_CHANGED_SELF, "%prefix% <white>Deine Fluggeschwindigkeit is jetzt <green>%speed%");
        file.setDefault(FLY_SPEED_CHANGED_OTHERS, "%prefix% <green>%player%'s <white>Fluggeschwindigkeit ist jetzt <green>%speed%");
        file.setDefault(REPAIRED_ALL, "%prefix% <white>Alle deine Items wurden repariert");
        file.setDefault(REPAIRED_ITEM, "%prefix% <white>Dein Item wurde repariert");
        file.setDefault(ITEM_RENAME_FAIL, "%prefix% <red>Dein Item konnte nicht umbenannt werden");
        file.setDefault(ITEM_RENAME_SUCCESS, "%prefix% <white>Dein Item wurde erfolgreich umbenannt");
        file.setDefault(ITEM_HEAD_VALUE, "%prefix% <gray>Kopf Wert<dark_gray>: " +
                "<click:copy_to_clipboard:\"%full-value%\"><hover:show_text:\"Klicken, um in die Zwischenablage zu kopieren\">" +
                "<white>%value%");
        file.setDefault(ITEM_HEAD_PLAYER, "%prefix% <gray>Kopf Besitzer<dark_gray>: <white>%owner%");
        file.setDefault(ITEM_HEAD_VALUE, "%prefix% <gray>Kopf URL<dark_gray>: " +
                "<click:copy_to_clipboard:\"%full-url%\"><hover:show_text:\"Klicken, um in die Zwischenablage zu kopieren\">" +
                "<white>%url%");
        file.setDefault(ITEM_HEAD_NONE, "%prefix% <red>Es konnte keine passenden Kopfdaten gefunden werden");
        file.setDefault(ITEM_HEAD_RECEIVED, "%prefix% <white>Du hast einen Spieler Kopf erhalten");
        file.setDefault(GOD_MODE_ACTIVE_SELF, "%prefix% <white>Du bist jetzt unverwundbar");
        file.setDefault(GOD_MODE_ACTIVE_OTHERS, "%prefix% <green>%player% <white>ist jetzt unverwundbar");
        file.setDefault(GOD_MODE_INACTIVE_SELF, "%prefix% <white>Du bist nicht länger unverwundbar");
        file.setDefault(GOD_MODE_INACTIVE_OTHERS, "%prefix% <green>%player% <white>ist nicht länger unverwundbar");
        file.save();
    }

    private static void initRoot() {
        var file = MessageFile.ROOT;
        file.setDefault(PREFIX, "<white>Tweaks <dark_gray>»<reset>");
        file.setDefault(COMMAND_USAGE, "%prefix% <red>%usage%");
        file.save();
    }
}
