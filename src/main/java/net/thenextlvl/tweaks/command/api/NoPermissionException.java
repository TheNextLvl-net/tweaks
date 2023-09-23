package net.thenextlvl.tweaks.command.api;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class NoPermissionException extends CommandException {
    private final String permission;

    @Override
    public void handle(CommandSender sender) {
        TweaksPlugin.get().bundle().sendMessage(sender, "command.permission",
                Placeholder.parsed("permission", permission));
    }
}
