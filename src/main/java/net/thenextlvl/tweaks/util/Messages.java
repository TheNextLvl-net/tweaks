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
    public static final MessageKey<CommandSender> PLAYER_NOT_ONLINE = new MessageKey<>("player.online.not", plugin.formatter());
    public static final MessageKey<CommandSender> WORLD_NOT_AFFECTED = new MessageKey<>("world.not.affected", plugin.formatter());
    public static final MessageKey<CommandSender> WORLD_NOT_FOUND = new MessageKey<>("world.not.found", plugin.formatter());
    public static final MessageKey<CommandSender> SATISFIED_HUNGER_SELF = new MessageKey<>("hunger.satisfied.self", plugin.formatter());
    public static final MessageKey<CommandSender> SATISFIED_HUNGER_OTHERS = new MessageKey<>("hunger.satisfied.self", plugin.formatter());


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
        file.setDefault(WORLD_NOT_AFFECTED, "%prefix% §cThe world §4%world%§c is not affected");
        file.setDefault(WORLD_NOT_FOUND, "%prefix% §cThe world §4%world%§c does not exist");
        file.setDefault(SATISFIED_HUNGER_SELF, "%prefix% §aYour hunger has been satisfied");
        file.setDefault(SATISFIED_HUNGER_OTHERS, "%prefix% §6%player%'s§a hunger has been satisfied");
        file.save();
    }

    private static void initGerman() {
        var file = MessageFile.getOrCreate(Locale.forLanguageTag("de-DE"));
        file.setDefault(COMMAND_PERMISSION, "%prefix%§c Darauf hast du keine rechte §8(§4%permission%§8)");
        file.setDefault(COMMAND_SENDER, "%prefix%§c Du kannst diesen command nicht nutzen");
        file.setDefault(PLAYER_NOT_ONLINE, "%prefix% §cDer Spieler §4%player%§c ist nicht online");
        file.setDefault(WORLD_NOT_AFFECTED, "%prefix% §cDie Welt §4%world%§c ist nicht betroffen");
        file.setDefault(WORLD_NOT_FOUND, "%prefix% §cDie Welt §4%world%§c existiert nicht");
        file.setDefault(SATISFIED_HUNGER_SELF, "%prefix% §aDein Hunger wurde gestillt");
        file.setDefault(SATISFIED_HUNGER_OTHERS, "%prefix% §6%player%'s§a Hunger wurde gestillt");
        file.save();
    }

    private static void initRoot() {
        var file = MessageFile.ROOT;
        file.setDefault(PREFIX, "§fTweaks §8»§r");
        file.setDefault(COMMAND_USAGE, "%prefix% §c%usage%");
        file.save();
    }
}
