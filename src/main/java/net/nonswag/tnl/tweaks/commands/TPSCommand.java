package net.nonswag.tnl.tweaks.commands;

import net.nonswag.tnl.listener.api.command.CommandSource;
import net.nonswag.tnl.listener.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;

public class TPSCommand extends TNLCommand {

    public TPSCommand() {
        super("tps", "tnl.tps");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        StringBuilder s = new StringBuilder("%prefix%§7 TPS from last 5s§8, §71m§8, §75m§8, §715m§8: §6");
        double[] tps = Bukkit.getTPS();
        for (int i = 0; i < tps.length; i++) {
            s.append(format(tps[i]));
            if (i < tps.length - 1) s.append("§8, §a");
        }
        double free = (Runtime.getRuntime().freeMemory() / 1024d) / 1024d;
        double total = (Runtime.getRuntime().totalMemory() / 1024d) / 1024d;
        double max = (Runtime.getRuntime().maxMemory() / 1024d) / 1024d;
        double used = (total - free);
        source.sendMessage(s.toString());
        source.sendMessage("%prefix%§7 Memory free§8: §6" + ((int) free) + "mb");
        source.sendMessage("%prefix%§7 Memory used§8: §6" + ((int) used) + "mb");
        source.sendMessage("%prefix%§7 Memory total§8: §6" + ((int) total) + "mb");
        source.sendMessage("%prefix%§7 Memory max§8: §6" + ((int) max) + "mb");
        source.sendMessage("%prefix%§7 Memory display§8: " + format((int) max, (int) used));
        source.sendMessage("%prefix%§7 Available processors§8: §a" + Runtime.getRuntime().availableProcessors());
    }

    @Nonnull
    private String format(int maxRam, int usedRam) {
        float percent = (usedRam * (100f / maxRam));
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            if ((percent / 100) * 20 <= i + 1) s.append("§7|");
            else s.append("§4|");
        }
        return "§6" + usedRam + "§8/§6" + maxRam + "mb §8» §6" + (((int) (usedRam * (100f / maxRam)))) + "§8/§6100% §8[" + s + "§8]";
    }

    @Nonnull
    private String format(double tps) {
        double rounded = (double) Math.round(tps * 100.0D) / 100.0D;
        return (rounded >= 18.0D ? "§a" : (rounded >= 16.0D ? "§e" : "§c")) + (rounded >= 20.0D ? "*" : "") + Math.min(rounded, 20.0D);
    }
}
