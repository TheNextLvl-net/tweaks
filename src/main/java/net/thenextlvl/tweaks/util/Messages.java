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
    public static final MessageKey<CommandSender> LORE_EDIT_TIP = new MessageKey<>("lore.edit.tip", plugin.formatter());
    public static final MessageKey<CommandSender> INVALID_ENCHANTMENT = new MessageKey<>("enchantment.invalid", plugin.formatter());
    public static final MessageKey<CommandSender> ENCHANTMENT_NOT_APPLICABLE = new MessageKey<>("enchantment.not.applicable", plugin.formatter());
    public static final MessageKey<CommandSender> ENCHANTED_ITEM = new MessageKey<>("enchantment.applied", plugin.formatter());

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
        file.setDefault(WORLD_NOT_AFFECTED, "%prefix% <red>The world <dark_red>%world%<red> is not affected");
        file.setDefault(WORLD_NOT_FOUND, "%prefix% <red>The world <dark_red>%world%<red> does not exist");
        file.setDefault(SATISFIED_HUNGER_SELF, "%prefix% <green>Your hunger has been satisfied");
        file.setDefault(SATISFIED_HUNGER_OTHERS, "%prefix% <gold>%player%'s<green> hunger has been satisfied");
        file.setDefault(RESTORED_HEALTH_SELF, "%prefix% <green>Your health has been restored");
        file.setDefault(RESTORED_HEALTH_OTHERS, "%prefix% <gold>%player%'s<green> health has been restored");
        file.setDefault(DISABLED_FLIGHT_OTHERS, "%prefix% <gold>%player%<green> can no longer fly");
        file.setDefault(ENABLED_FLIGHT_OTHERS, "%prefix% <gold>%player%'s<green> is now able to fly");
        file.setDefault(DISABLED_FLIGHT_SELF, "%prefix% <green>You can no longer fly");
        file.setDefault(ENABLED_FLIGHT_SELF, "%prefix% <green>You are now able to fly");
        file.setDefault(PING_SELF, "%prefix% <green>Your connection latency is around <gold>%ping%ms");
        file.setDefault(PING_OTHERS, "%prefix% <gold>%player%'s<green> connection latency is around <gold>%ping%ms");
        file.setDefault(HAT_EQUIPPED, "%prefix% <green>You have equipped your item as a hat");
        file.setDefault(HOLD_ITEM, "%prefix% <red>You have to hold an item in your main hand");
        file.setDefault(LAST_SEEN_NOW, "%prefix% <gold>%player%<green> is online at the moment");
        file.setDefault(LAST_SEEN_TIME, "%prefix% <gold>%player%<green> was last seen <gold>%time%");
        file.setDefault(BACK_EMPTY, "%prefix% <red>You can't go back any further");
        file.setDefault(BACK_TELEPORT_SUCCESS, "%prefix% <green>Teleported you to your last position");
        file.setDefault(BACK_TELEPORT_FAIL, "%prefix% <red>Failed to teleport you to your last position");
        file.setDefault(TIME_DAY, "%prefix% <green>It is now day in <gold>%world%");
        file.setDefault(TIME_NIGHT, "%prefix% <green>It is now night in <gold>%world%");
        file.setDefault(WEATHER_RAIN, "%prefix% <green>It is now raining in <gold>%world%");
        file.setDefault(WEATHER_SUN, "%prefix% <green>It is now sunny in <gold>%world%");
        file.setDefault(WEATHER_THUNDER, "%prefix% <green>It is now thundering in <gold>%world%");
        file.setDefault(LORE_EDIT_TIP, "%prefix% <red>Use <dark_red>\\n<red> to add a new line and <dark_red>\\t<red> to add indentations");
        file.setDefault(INVALID_ENCHANTMENT, "%prefix% <dark_red>%enchantment%<red> is not a valid enchantment");
        file.setDefault(ENCHANTMENT_NOT_APPLICABLE, "<red>This enchantment is not applicable on <dark_red><item>");
        file.setDefault(ENCHANTED_ITEM, "%prefix% <green>Applied <gold><enchantment><green> to your item");
        file.save();
    }

    private static void initGerman() {
        var file = MessageFile.getOrCreate(Locale.forLanguageTag("de-DE"));
        file.setDefault(COMMAND_PERMISSION, "%prefix% <red>Darauf hast du keine rechte <dark_gray>(<dark_red>%permission%<dark_gray>)");
        file.setDefault(COMMAND_SENDER, "%prefix% <red>Du kannst diesen command nicht nutzen");
        file.setDefault(PLAYER_NOT_ONLINE, "%prefix% <red>Der Spieler <dark_red>%player%<red> ist nicht online");
        file.setDefault(PLAYER_NOT_FOUND, "%prefix% <red>Der Spieler <dark_red>%player%<red> ist uns nicht bekannt");
        file.setDefault(WORLD_NOT_AFFECTED, "%prefix% <red>Die Welt <dark_red>%world%<red> ist nicht betroffen");
        file.setDefault(WORLD_NOT_FOUND, "%prefix% <red>Die Welt <dark_red>%world%<red> existiert nicht");
        file.setDefault(SATISFIED_HUNGER_SELF, "%prefix% <green>Dein Hunger wurde gestillt");
        file.setDefault(SATISFIED_HUNGER_OTHERS, "%prefix% <gold>%player%'s<green> Hunger wurde gestillt");
        file.setDefault(RESTORED_HEALTH_SELF, "%prefix% <green>Du wurdest vollständig geheilt");
        file.setDefault(RESTORED_HEALTH_OTHERS, "%prefix% <gold>%player%<green> wurde vollständig geheilt");
        file.setDefault(DISABLED_FLIGHT_OTHERS, "%prefix% <gold>%player%<green> kann nicht länger fliegen");
        file.setDefault(ENABLED_FLIGHT_OTHERS, "%prefix% <gold>%player%'s<green> kann ab jetzt fliegen");
        file.setDefault(DISABLED_FLIGHT_SELF, "%prefix% <green>Du kannst nicht länger fliegen");
        file.setDefault(ENABLED_FLIGHT_SELF, "%prefix% <green>Du kannst ab jetzt fliegen");
        file.setDefault(PING_SELF, "%prefix% <green>Du hast einen verbindungslatenz von <gold>%ping%ms");
        file.setDefault(PING_OTHERS, "%prefix% <gold>%player%<green> hat eine verbindungslatenz von <gold>%ping%ms");
        file.setDefault(HOLD_ITEM, "%prefix% <red>Du musst ein Item in der Haupthand halten");
        file.setDefault(LAST_SEEN_NOW, "%prefix% <gold>%player%<green> is aktuell online");
        file.setDefault(LAST_SEEN_TIME, "%prefix% <gold>%player%<green> war zuletzt online <gold>%time%");
        file.setDefault(BACK_EMPTY, "%prefix% <red>Du kannst nicht weiter zurück gehen");
        file.setDefault(BACK_TELEPORT_SUCCESS, "%prefix% <green>Du wurdest zu deiner letzten Position teleportiert");
        file.setDefault(BACK_TELEPORT_FAIL, "%prefix% <red>Du konntest nicht zu deiner letzten Position teleportiert werden");
        file.setDefault(TIME_DAY, "%prefix% <green>Es ist jetzt tag in <gold>%world%");
        file.setDefault(TIME_NIGHT, "%prefix% <green>Es ist jetzt nacht in <gold>%world%");
        file.setDefault(WEATHER_RAIN, "%prefix% <green>Es regnet jetzt in <gold>%world%");
        file.setDefault(WEATHER_SUN, "%prefix% <green>Es scheint jetzt die sonne in <gold>%world%");
        file.setDefault(WEATHER_THUNDER, "%prefix% <green>Es gewittert jetzt in <gold>%world%");
        file.setDefault(LORE_EDIT_TIP, "%prefix% <red>Nutze <dark_red>\\n<red> für neue Zeile und <dark_red>\\t<red> für Einrückungen");
        file.setDefault(INVALID_ENCHANTMENT, "%prefix% <dark_red>%enchantment%<red> ist keine gültige Verzauberung");
        file.setDefault(ENCHANTMENT_NOT_APPLICABLE, "%prefix% <red>Dieses enchantment ist nicht auf <dark_red><item><red> anwendbar");
        file.setDefault(ENCHANTED_ITEM, "%prefix% <green>Dein Item wurde mit <gold><enchantment><green> verzaubert");
        file.save();
    }

    private static void initRoot() {
        var file = MessageFile.ROOT;
        file.setDefault(PREFIX, "<white>Tweaks <dark_gray>»<reset>");
        file.setDefault(COMMAND_USAGE, "%prefix% <red>%usage%");
        file.save();
    }
}
