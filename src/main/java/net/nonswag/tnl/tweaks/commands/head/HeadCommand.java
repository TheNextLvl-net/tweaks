package net.nonswag.tnl.tweaks.commands.head;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.nonswag.core.api.command.CommandSource;
import net.nonswag.tnl.listener.api.command.simple.SimpleCommand;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HeadCommand extends SimpleCommand {

    public HeadCommand() {
        super("head", "tnl.head");
        addSubCommand(new Player());
        addSubCommand(new Value());
        addSubCommand(new URL());
    }

    @Override
    public boolean canUse(@Nonnull CommandSource source) {
        return source instanceof TNLPlayer;
    }

    @Nullable
    static String value(@Nonnull SkullMeta skull) {
        PlayerProfile profile = skull.getPlayerProfile();
        if (profile == null || !profile.hasTextures()) return null;
        for (ProfileProperty property : profile.getProperties()) {
            if (!property.getName().equalsIgnoreCase("textures")) continue;
            return property.getValue();
        }
        return null;
    }
}
