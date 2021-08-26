package net.nonswag.tnl.tweaks;

import net.nonswag.tnl.listener.api.command.CommandManager;
import net.nonswag.tnl.listener.api.plugin.PluginUpdate;
import net.nonswag.tnl.listener.api.settings.Settings;
import net.nonswag.tnl.tweaks.commands.*;
import org.bukkit.plugin.java.JavaPlugin;

public class Tweaks extends JavaPlugin {

    @Override
    public void onEnable() {
        CommandManager.registerCommands(new PingCommand(), new TPSCommand(), new DayCommand(),
                new NightCommand(), new SunCommand(), new RainCommand(), new ThunderCommand(),
                new RightsCommand(), new OPCommand(), new DeOPCommand(), new FeedCommand(),
                new HealCommand(), new FlyCommand(), new SpeedCommand(), new GamemodeCommand());
        if (Settings.AUTO_UPDATER.getValue()) new PluginUpdate(this).downloadUpdate();
    }
}
