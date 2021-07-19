package net.nonswag.tnl.tweaks;

import net.nonswag.tnl.listener.api.command.CommandManager;
import net.nonswag.tnl.listener.api.event.EventManager;
import net.nonswag.tnl.listener.api.plugin.PluginUpdate;
import net.nonswag.tnl.listener.api.settings.Settings;
import net.nonswag.tnl.tweaks.commands.*;
import net.nonswag.tnl.tweaks.completer.*;
import net.nonswag.tnl.tweaks.listeners.CommandListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Tweaks extends JavaPlugin {

    @Override
    public void onEnable() {
        CommandManager commandManager = new CommandManager(this);
        EventManager eventManager = new EventManager(this);
        commandManager.registerCommand("ping", new PingCommand(), new PingCommandTabCompleter());
        commandManager.registerCommand("tps", "tnl.tps", new TPSCommand(), new TPSCommandTabCompleter());
        commandManager.registerCommand("day", "tnl.day", new DayCommand(), new DayCommandTabCompleter());
        commandManager.registerCommand("night", "tnl.night", new NightCommand(), new NightCommandTabCompleter());
        commandManager.registerCommand("sun", "tnl.sun", new SunCommand(), new SunCommandTabCompleter());
        commandManager.registerCommand("rain", "tnl.rain", new RainCommand(), new RainCommandTabCompleter());
        commandManager.registerCommand("thunder", "tnl.thunder", new ThunderCommand(), new ThunderCommandTabCompleter());
        commandManager.registerCommand("gamemode", "tnl.gamemode", new GamemodeCommand(), new GamemodeCommandTabCompleter());
        commandManager.registerCommand("heal", "tnl.heal", new HealCommand(), new HealCommandTabCompleter());
        commandManager.registerCommand("feed", "tnl.feed", new FeedCommand(), new FeedCommandTabCompleter());
        commandManager.registerCommand("fly", "tnl.fly", new FlyCommand(), new FlyCommandTabCompleter());
        commandManager.registerCommand("rights", "tnl.rights", new RightsCommand(), new RightsCommandTabCompleter());
        commandManager.registerCommand("op", "tnl.rights", new OPCommand(), new OPCommandTabCompleter());
        commandManager.registerCommand("deop", "tnl.rights", new DeOPCommand(), new DeOPCommandTabCompleter());
        commandManager.registerCommand("speed", "tnl.speed", new SpeedCommand(), new SpeedCommandTabCompleter());
        eventManager.registerListener(new CommandListener());
        if (Settings.AUTO_UPDATER.getValue()) new PluginUpdate(this).downloadUpdate();
    }
}
