package net.thenextlvl.tweaks.controller;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.WeakHashMap;

@NullMarked
@RequiredArgsConstructor
public class MSGController {
    private final Map<CommandSender, CommandSender> conversations = new WeakHashMap<>();

    public @Nullable CommandSender getConversation(CommandSender sender) {
        return conversations.get(sender);
    }

    public boolean isConversing(CommandSender sender, CommandSender receiver) {
        return receiver.equals(conversations.get(sender));
    }

    public void startConversation(CommandSender sender, CommandSender receiver) {
        conversations.put(sender, receiver);
    }

    public void removeConversation(CommandSender sender) {
        conversations.remove(sender);
    }

    public void removeConversations(CommandSender sender) {
        removeConversation(sender);
        removeExternalConversations(sender);
    }

    public void removeExternalConversations(CommandSender sender) {
        conversations.entrySet().removeIf(entry -> entry.getValue().equals(sender));
    }
}
