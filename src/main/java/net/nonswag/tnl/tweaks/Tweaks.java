package net.nonswag.tnl.tweaks;

import net.nonswag.core.api.annotation.FieldsAreNullableByDefault;
import net.nonswag.core.api.annotation.MethodsReturnNonnullByDefault;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.plugin.TNLPlugin;
import net.nonswag.tnl.tweaks.api.manager.PositionManager;
import net.nonswag.tnl.tweaks.commands.*;
import net.nonswag.tnl.tweaks.commands.head.HeadCommand;
import net.nonswag.tnl.tweaks.listeners.DeathListener;
import net.nonswag.tnl.tweaks.listeners.TeleportListener;
import net.nonswag.tnl.tweaks.utils.Messages;

@FieldsAreNullableByDefault
@MethodsReturnNonnullByDefault
public class Tweaks extends TNLPlugin {
    private static Tweaks instance;

    @Override
    public void enable() {
        instance = this;
        validateUptime();
        registerManagers();
        registerCommands();
        registerListeners();
        Messages.loadAll();
    }

    public static long getUptime() {
        validateUptime();
        return System.currentTimeMillis() - (long) System.getProperties().get("uptime");
    }

    private static void validateUptime() {
        if (System.getProperties().get("uptime") instanceof Long) return;
        System.getProperties().put("uptime", System.currentTimeMillis());
    }

    private void registerManagers() {
        getRegistrationManager().registerManager(PositionManager.class, player -> new PositionManager() {
            @Override
            public TNLPlayer getPlayer() {
                return player;
            }
        });
    }

    private void registerListeners() {
        getEventManager().registerListener(new DeathListener());
        getEventManager().registerListener(new TeleportListener());
    }

    private void registerCommands() {
        getCommandManager().registerCommand(new OPCommand());
        getCommandManager().registerCommand(new TPSCommand());
        getCommandManager().registerCommand(new DayCommand());
        getCommandManager().registerCommand(new FlyCommand());
        getCommandManager().registerCommand(new SunCommand());
        getCommandManager().registerCommand(new BackCommand());
        getCommandManager().registerCommand(new PingCommand());
        getCommandManager().registerCommand(new HeadCommand());
        getCommandManager().registerCommand(new ItemCommand());
        getCommandManager().registerCommand(new RainCommand());
        getCommandManager().registerCommand(new DeOPCommand());
        getCommandManager().registerCommand(new FeedCommand());
        getCommandManager().registerCommand(new HealCommand());
        getCommandManager().registerCommand(new SpeedCommand());
        getCommandManager().registerCommand(new NightCommand());
        getCommandManager().registerCommand(new ClearCommand());
        getCommandManager().registerCommand(new RepairCommand());
        getCommandManager().registerCommand(new EnchantCommand());
        getCommandManager().registerCommand(new ThunderCommand());
        getCommandManager().registerCommand(new GamemodeCommand());
        getCommandManager().registerCommand(new UnEnchantCommand());
        getCommandManager().registerCommand(new InventoryCommand());
        getCommandManager().registerCommand(new EnderChestCommand());
    }

    public static Tweaks getInstance() {
        assert instance != null;
        return instance;
    }
}
