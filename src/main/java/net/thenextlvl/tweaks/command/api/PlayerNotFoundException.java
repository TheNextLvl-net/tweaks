package net.thenextlvl.tweaks.command.api;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class PlayerNotFoundException extends CommandException {
    private final String input;

    @Override
    public void handle(CommandSender sender) {
        TweaksPlugin.get().bundle().sendMessage(sender, "player.not.found", Placeholder.parsed("player", input));
    }
}
