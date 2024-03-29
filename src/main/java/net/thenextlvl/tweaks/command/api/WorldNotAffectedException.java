package net.thenextlvl.tweaks.command.api;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class WorldNotAffectedException extends CommandException {
    private final World world;

    @Override
    public void handle(CommandSender sender) {
        TweaksPlugin.get().bundle().sendMessage(sender, "world.not.affected",
                Placeholder.parsed("world", world.getName()));
    }
}
