package net.thenextlvl.tweaks.command.api;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class PlayerNotFoundException extends CommandException {
    private final String input;

    @Override
    public void handle(CommandSender sender) {
        sender.sendRichMessage("<lang:tweaks.player.not.found>", Placeholder.parsed("player", input));
    }
}
