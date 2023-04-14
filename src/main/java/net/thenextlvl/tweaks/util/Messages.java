package net.thenextlvl.tweaks.util;

import core.api.file.format.MessageFile;
import core.api.placeholder.MessageKey;
import core.api.placeholder.SystemMessageKey;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

public class Messages {
    private static final TweaksPlugin plugin = JavaPlugin.getPlugin(TweaksPlugin.class);
    public static final SystemMessageKey<CommandSender> PREFIX = new SystemMessageKey<>("tweaks.prefix", plugin.formatter());
    public static final SystemMessageKey<CommandSender> COMMAND_USAGE = new SystemMessageKey<>("command.usage", plugin.formatter());
    public static final MessageKey<CommandSender> COMMAND_PERMISSION = new MessageKey<>("command.permission", plugin.formatter());
    public static final MessageKey<CommandSender> COMMAND_SENDER = new MessageKey<>("command.sender", plugin.formatter());


    static {
        initEnglish();
        initGerman();
        initRoot();
    }

    private static void initEnglish() {
        var file = MessageFile.getOrCreate(Locale.forLanguageTag("en-US"));
        file.setDefault(COMMAND_PERMISSION, "%prefix% §cYou have no rights §8(§4%permission%§8)");
        file.setDefault(COMMAND_SENDER, "%prefix% §cYou cannot use this command");
        file.save();
    }

    private static void initGerman() {
        var file = MessageFile.getOrCreate(Locale.forLanguageTag("de-DE"));
        file.setDefault(COMMAND_PERMISSION, "%prefix%§c Darauf hast du keine rechte §8(§4%permission%§8)");
        file.setDefault(COMMAND_SENDER, "%prefix%§c Du kannst diesen command nicht nutzen");
        file.save();
    }

    private static void initRoot() {
        var file = MessageFile.ROOT;
        file.setDefault(PREFIX, "§fTweaks §8»§r");
        file.setDefault(COMMAND_USAGE, "%prefix% §c%usage%");
        file.save();
    }
}
