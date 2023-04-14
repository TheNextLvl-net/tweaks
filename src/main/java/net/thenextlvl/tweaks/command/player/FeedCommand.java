package net.thenextlvl.tweaks.command.player;

import net.thenextlvl.tweaks.command.api.CommandInfo;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "feed", usage = "/<command> (player)", permission = "tweaks.command.feed")
public class FeedCommand extends PlayerCommand {

    @Override
    protected void execute(CommandSender sender, Player value) {
        value.setFoodLevel(20);
        value.setSaturation(10f);
        value.setExhaustion(0f);
        // TODO: Your hunger was satisfied
    }

}
