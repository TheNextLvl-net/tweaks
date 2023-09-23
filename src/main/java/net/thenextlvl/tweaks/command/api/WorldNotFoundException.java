package net.thenextlvl.tweaks.command.api;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class WorldNotFoundException extends CommandException {
    private final String input;

    @Override
    public void handle(CommandSender sender) {
        TweaksPlugin.get().bundle().sendMessage(sender, "world.not.found", Placeholder.parsed("world", input));
    }
}
