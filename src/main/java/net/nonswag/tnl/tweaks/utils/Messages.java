package net.nonswag.tnl.tweaks.utils;

import net.nonswag.tnl.core.api.file.formats.MessageFile;
import net.nonswag.tnl.core.api.message.Message;
import net.nonswag.tnl.core.api.message.key.MessageKey;

import javax.annotation.Nonnull;

public final class Messages {

    @Nonnull
    public static MessageKey TELEPORTED = new MessageKey("teleported");
    @Nonnull
    public static MessageKey NO_POSITION = new MessageKey("no-position");

    private Messages() {
    }

    public static void init() {
        MessageFile english = Message.getEnglish();
        MessageFile german = Message.getGerman();

        english.setDefault(TELEPORTED, "%prefix% §aYou got teleported");
        english.setDefault(NO_POSITION, "%prefix% §cNo last position set");
        english.save();

        german.setDefault(TELEPORTED, "%prefix% §aDu wurdest teleportiert");
        german.setDefault(NO_POSITION, "%prefix% §cKeine letzte position gesetzt");
        german.save();
    }
}
