package net.nonswag.tnl.tweaks;

import net.nonswag.tnl.listener.api.plugin.PluginUpdate;
import net.nonswag.tnl.listener.api.plugin.TNLPlugin;
import net.nonswag.tnl.listener.api.settings.Settings;
import net.nonswag.tnl.tweaks.commands.*;

public class Tweaks extends TNLPlugin {

    @Override
    public void onEnable() {
        getCommandManager().registerCommands(new PingCommand(), new TPSCommand(), new DayCommand(),
                new NightCommand(), new SunCommand(), new RainCommand(), new ThunderCommand(),
                new OPCommand(), new DeOPCommand(), new FeedCommand(), new HealCommand(),
                new FlyCommand(), new SpeedCommand(), new GamemodeCommand(), new InventoryCommand(),
                new HeadCommand(), new EnderChestCommand(), new ItemCommand(), new EnchantCommand(),
                new UnEnchantCommand());
        if (Settings.AUTO_UPDATER.getValue()) new PluginUpdate(this).downloadUpdate();
    }
}
