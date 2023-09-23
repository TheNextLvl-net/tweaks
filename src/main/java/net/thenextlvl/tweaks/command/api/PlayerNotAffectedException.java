package net.thenextlvl.tweaks.command.api;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class PlayerNotAffectedException extends CommandException {
    private final OfflinePlayer player;

    @Override
    public void handle(CommandSender sender) {
        TweaksPlugin.get().bundle().sendMessage(sender, "player.not.affected", Placeholder.parsed("player",
                player.getName() != null ? player.getName() : "null"));
    }
}
