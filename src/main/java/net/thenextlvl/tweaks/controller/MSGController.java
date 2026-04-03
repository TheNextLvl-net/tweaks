package net.thenextlvl.tweaks.controller;

import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.WeakHashMap;

@NullMarked
public final class MSGController {
    private final Map<CommandSender, CommandSender> conversations = new WeakHashMap<>();

    public @Nullable CommandSender getConversation(final CommandSender sender) {
        return conversations.get(sender);
    }

    public boolean isConversing(final CommandSender sender, final CommandSender receiver) {
        return receiver.equals(conversations.get(sender));
    }

    public void startConversation(final CommandSender sender, final CommandSender receiver) {
        conversations.put(sender, receiver);
    }

    public void removeConversation(final CommandSender sender) {
        conversations.remove(sender);
    }

    public void removeConversations(final CommandSender sender) {
        removeConversation(sender);
        removeExternalConversations(sender);
    }

    public void removeExternalConversations(final CommandSender sender) {
        conversations.entrySet().removeIf(entry -> entry.getValue().equals(sender));
    }
}
