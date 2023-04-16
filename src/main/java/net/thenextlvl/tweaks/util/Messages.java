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
    public static final MessageKey<CommandSender> HAT_EQUIPPED = new MessageKey<>("hat.equipped", plugin.formatter());
    public static final MessageKey<CommandSender> HOL_ITEM = new MessageKey<>("hold.item", plugin.formatter());
    public static final MessageKey<CommandSender> LAST_SEEN_NOW = new MessageKey<>("last.seen.now", plugin.formatter());
    public static final MessageKey<CommandSender> LAST_SEEN_TIME = new MessageKey<>("last.seen.time", plugin.formatter());
    public static final MessageKey<CommandSender> BACK_EMPTY = new MessageKey<>("back.empty", plugin.formatter());
    public static final MessageKey<CommandSender> BACK_TELEPORT_SUCCESS = new MessageKey<>("back.teleport.success", plugin.formatter());
    public static final MessageKey<CommandSender> BACK_TELEPORT_FAIL = new MessageKey<>("back.teleport.fail", plugin.formatter());

    static {
        initEnglish();
        initGerman();
        initRoot();
    }

    private static void initEnglish() {
        var file = MessageFile.getOrCreate(ENGLISH);
        file.setDefault(COMMAND_PERMISSION, "%prefix% §cYou have no rights §8(§4%permission%§8)");
        file.setDefault(COMMAND_SENDER, "%prefix% §cYou cannot use this command");
        file.setDefault(PLAYER_NOT_ONLINE, "%prefix% §cThe player §4%player%§c is not online");
        file.setDefault(PLAYER_NOT_FOUND, "%prefix% §cThe player §4%player%§c is unknown to us");
        file.setDefault(WORLD_NOT_AFFECTED, "%prefix% §cThe world §4%world%§c is not affected");
        file.setDefault(WORLD_NOT_FOUND, "%prefix% §cThe world §4%world%§c does not exist");
        file.setDefault(SATISFIED_HUNGER_SELF, "%prefix% §aYour hunger has been satisfied");
        file.setDefault(SATISFIED_HUNGER_OTHERS, "%prefix% §6%player%'s§a hunger has been satisfied");
        file.setDefault(RESTORED_HEALTH_SELF, "%prefix% §aYour health has been restored");
        file.setDefault(RESTORED_HEALTH_OTHERS, "%prefix% §6%player%'s§a health has been restored");
        file.setDefault(DISABLED_FLIGHT_OTHERS, "%prefix% §6%player%§a can no longer fly");
        file.setDefault(ENABLED_FLIGHT_OTHERS, "%prefix% §6%player%'s§a is now able to fly");
        file.setDefault(DISABLED_FLIGHT_SELF, "%prefix% §aYou can no longer fly");
        file.setDefault(ENABLED_FLIGHT_SELF, "%prefix% §aYou are now able to fly");
        file.setDefault(HAT_EQUIPPED, "%prefix% §aYou have equipped your item as a hat");
        file.setDefault(HOL_ITEM, "%prefix% §cYou have to hold an item in your main hand");
        file.setDefault(LAST_SEEN_NOW, "%prefix% §6%player%§a is online at the moment");
        file.setDefault(LAST_SEEN_TIME, "%prefix% §6%player%§a was last seen §6%time%");
        file.setDefault(BACK_EMPTY, "%prefix% §cYou can't go back any further");
        file.setDefault(BACK_TELEPORT_SUCCESS, "%prefix% §aTeleported you to your last position");
        file.setDefault(BACK_TELEPORT_FAIL, "%prefix% §cFailed to teleport you to your last position");
        file.save();
    }

    private static void initGerman() {
        var file = MessageFile.getOrCreate(Locale.forLanguageTag("de-DE"));
        file.setDefault(COMMAND_PERMISSION, "%prefix% §cDarauf hast du keine rechte §8(§4%permission%§8)");
        file.setDefault(COMMAND_SENDER, "%prefix% §cDu kannst diesen command nicht nutzen");
        file.setDefault(PLAYER_NOT_ONLINE, "%prefix% §cDer Spieler §4%player%§c ist nicht online");
        file.setDefault(PLAYER_NOT_FOUND, "%prefix% §cDer Spieler §4%player%§c ist uns nicht bekannt");
        file.setDefault(WORLD_NOT_AFFECTED, "%prefix% §cDie Welt §4%world%§c ist nicht betroffen");
        file.setDefault(WORLD_NOT_FOUND, "%prefix% §cDie Welt §4%world%§c existiert nicht");
        file.setDefault(SATISFIED_HUNGER_SELF, "%prefix% §aDein Hunger wurde gestillt");
        file.setDefault(SATISFIED_HUNGER_OTHERS, "%prefix% §6%player%'s§a Hunger wurde gestillt");
        file.setDefault(RESTORED_HEALTH_SELF, "%prefix% §aDu wurdest vollständig geheilt");
        file.setDefault(RESTORED_HEALTH_OTHERS, "%prefix% §6%player%§a wurde vollständig geheilt");
        file.setDefault(DISABLED_FLIGHT_OTHERS, "%prefix% §6%player%§a kann nicht länger fliegen");
        file.setDefault(ENABLED_FLIGHT_OTHERS, "%prefix% §6%player%'s§a kann ab jetzt fliegen");
        file.setDefault(DISABLED_FLIGHT_SELF, "%prefix% §aDu kannst nicht länger fliegen");
        file.setDefault(ENABLED_FLIGHT_SELF, "%prefix% §aDu kannst ab jetzt fliegen");
        file.setDefault(HOL_ITEM, "%prefix% §cDu musst ein Item in der Haupthand halten");
        file.setDefault(LAST_SEEN_NOW, "%prefix% §6%player%§a is aktuell online");
        file.setDefault(LAST_SEEN_TIME, "%prefix% §6%player%§a war zuletzt online §6%time%");
        file.setDefault(BACK_EMPTY, "%prefix% §cDu kannst nicht weiter zurück gehen");
        file.setDefault(BACK_TELEPORT_SUCCESS, "%prefix% §aDu wurdest zu deiner letzten Position teleportiert");
        file.setDefault(BACK_TELEPORT_FAIL, "%prefix% §cDu konntest nicht zu deiner letzten Position teleportiert werden");
        file.save();
    }

    private static void initRoot() {
        var file = MessageFile.ROOT;
        file.setDefault(PREFIX, "§fTweaks §8»§r");
        file.setDefault(COMMAND_USAGE, "%prefix% §c%usage%");
        file.save();
    }
}
