package net.nonswag.tnl.tweaks;

import net.nonswag.tnl.listener.api.plugin.PluginUpdate;
import net.nonswag.tnl.listener.api.plugin.TNLPlugin;
import net.nonswag.tnl.listener.api.settings.Settings;
import net.nonswag.tnl.tweaks.commands.*;
import net.nonswag.tnl.tweaks.listeners.DeathListener;
import net.nonswag.tnl.tweaks.listeners.TeleportListener;
import net.nonswag.tnl.tweaks.utils.Messages;

public class Tweaks extends TNLPlugin {

    @Override
    public void enable() {
        validateUptime();
        registerCommands();
        registerListeners();
        Messages.init();
        async(() -> {
            if (Settings.AUTO_UPDATER.getValue()) new PluginUpdate(this).downloadUpdate();
        });
    }

    public static long getUptime() {
        validateUptime();
        return System.currentTimeMillis() - (Long) System.getProperties().get("uptime");
    }

    private static void validateUptime() {
        if (!(System.getProperties().get("uptime") instanceof Long)) {
            System.getProperties().put("uptime", System.currentTimeMillis());
        }
    }

    private void registerListeners() {
        getEventManager().registerListener(new TeleportListener());
        getEventManager().registerListener(new DeathListener());
    }

    private void registerCommands() {
        getCommandManager().registerCommand(new PingCommand());
        getCommandManager().registerCommand(new TPSCommand());
        getCommandManager().registerCommand(new DayCommand());
        getCommandManager().registerCommand(new NightCommand());
        getCommandManager().registerCommand(new SunCommand());
        getCommandManager().registerCommand(new RainCommand());
        getCommandManager().registerCommand(new ThunderCommand());
        getCommandManager().registerCommand(new OPCommand());
        getCommandManager().registerCommand(new DeOPCommand());
        getCommandManager().registerCommand(new FeedCommand());
        getCommandManager().registerCommand(new HealCommand());
        getCommandManager().registerCommand(new FlyCommand());
        getCommandManager().registerCommand(new SpeedCommand());
        getCommandManager().registerCommand(new GamemodeCommand());
        getCommandManager().registerCommand(new InventoryCommand());
        getCommandManager().registerCommand(new HeadCommand());
        getCommandManager().registerCommand(new EnderChestCommand());
        getCommandManager().registerCommand(new ItemCommand());
        getCommandManager().registerCommand(new RepairCommand());
        getCommandManager().registerCommand(new EnchantCommand());
        getCommandManager().registerCommand(new UnEnchantCommand());
        getCommandManager().registerCommand(new ClearCommand());
        getCommandManager().registerCommand(new BackCommand());
    }
}
