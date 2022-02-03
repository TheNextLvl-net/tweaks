package net.nonswag.tnl.tweaks.commands;

import com.sun.management.OperatingSystemMXBean;
import net.nonswag.tnl.core.api.command.CommandSource;
import net.nonswag.tnl.core.api.command.Invocation;
import net.nonswag.tnl.core.utils.StringUtil;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.tweaks.Tweaks;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import java.lang.management.ManagementFactory;

public class TPSCommand extends TNLCommand {

    public TPSCommand() {
        super("tps", "tnl.tps");
    }

    @Override
    protected void execute(@Nonnull Invocation invocation) {
        CommandSource source = invocation.source();
        double[] tps = Bukkit.getTPS();
        StringBuilder s = new StringBuilder("%prefix%§7 TPS from last ");
        if (tps.length == 4) s.append("5s§8, §71m§8, §75m§8, §715m§8: §6");
        else s.append("§71m§8, §75m§8, §715m§8: §6");
        for (int i = 0; i < tps.length && i < 4; i++) {
            s.append(format(tps[i]));
            if (i < tps.length - 1) s.append("§8, §a");
        }
        double free = (Runtime.getRuntime().freeMemory() / 1024d) / 1024d;
        double total = (Runtime.getRuntime().totalMemory() / 1024d) / 1024d;
        double max = (Runtime.getRuntime().maxMemory() / 1024d) / 1024d;
        double used = (total - free);
        source.sendMessage(s.toString());
        source.sendMessage("%prefix%§7 Memory display§8: " + format((int) max, (int) used));
        source.sendMessage("%prefix%§7 Available processors§8: §a" + Runtime.getRuntime().availableProcessors());
        long seconds = Tweaks.getUptime() / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = minutes / 24;
        source.sendMessage("%prefix%§7 Uptime§8: §6" + days + "d§8, §6" +
                (hours % 24) + "h§8, §6" + (minutes % 60) + "m§8, §6" + (seconds % 60) + "s§8");
        OperatingSystemMXBean bean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        double load = bean.getCpuLoad();
        if (load == 0) load = bean.getCpuLoad();
        source.sendMessage("%prefix%§7 CPU Usage§8: §6" + StringUtil.format("#,##0.00", load * 100) + "%");
        /*
        For memory            OperatingSystemMXBean.getTotalPhysicalMemorySize() and OperatingSystemMXBean.getFreePhysicalMemorySize()
        For disk space        File.getTotalSpace() and File.getUsableSpace()
         */
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
