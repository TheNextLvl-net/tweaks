package net.nonswag.tnl.tweaks;

import net.nonswag.tnl.listener.api.plugin.PluginUpdate;
import net.nonswag.tnl.listener.api.plugin.TNLPlugin;
import net.nonswag.tnl.listener.api.settings.Settings;
import net.nonswag.tnl.tweaks.commands.*;
import net.nonswag.tnl.tweaks.listeners.DeathListener;
import net.nonswag.tnl.tweaks.listeners.TeleportListener;

public class Tweaks extends TNLPlugin {

    @Override
    public void onEnable() {
        getCommandManager().registerCommands(new PingCommand(), new TPSCommand(), new DayCommand(),
                new NightCommand(), new SunCommand(), new RainCommand(), new ThunderCommand(),
                new OPCommand(), new DeOPCommand(), new FeedCommand(), new HealCommand(),
                new FlyCommand(), new SpeedCommand(), new GamemodeCommand(), new InventoryCommand(),
                new HeadCommand(), new EnderChestCommand(), new ItemCommand(), new EnchantCommand(),
                new UnEnchantCommand(), new RepairCommand(), new ClearCommand(), new BackCommand());
        getEventManager().registerListener(new TeleportListener());
        getEventManager().registerListener(new DeathListener());
        if (Settings.AUTO_UPDATER.getValue()) new PluginUpdate(this).downloadUpdate();
    }
}
