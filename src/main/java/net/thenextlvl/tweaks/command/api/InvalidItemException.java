package net.thenextlvl.tweaks.command.api;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class InvalidItemException extends CommandException {
    private final String argument;

    @Override
    public void handle(CommandSender sender) {
        TweaksPlugin.get().bundle().sendMessage(sender, "item.invalid", Placeholder.parsed("item", argument));
    }
}
