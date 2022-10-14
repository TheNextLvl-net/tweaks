package net.nonswag.tnl.tweaks.commands;

import com.sun.management.OperatingSystemMXBean;
import net.nonswag.core.api.command.CommandSource;
import net.nonswag.core.api.command.Invocation;
import net.nonswag.tnl.listener.api.command.TNLCommand;
import net.nonswag.tnl.tweaks.Tweaks;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import java.io.File;
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
        source.sendMessage(s.toString());
        double mb = 1024d * 1024d;
        double gb = 1024d * 1024d * 1024d;
        double free = Runtime.getRuntime().freeMemory() / mb;
        double total = Runtime.getRuntime().totalMemory() / mb;
        double max = Runtime.getRuntime().maxMemory() / mb;
        double used = (total - free);
        source.sendMessage("%prefix%§7 Memory usage§8: " + format((int) max, (int) used, "MB", true));
        File file = new File("/");
        max = file.getTotalSpace() / gb;
        used = (file.getTotalSpace() - file.getFreeSpace()) / gb;
        source.sendMessage("%prefix%§7 Disk Usage§8: §6" + format((int) max, (int) used, "GB", true));
        long seconds = Tweaks.getUptime() / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        OperatingSystemMXBean bean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        double load = bean.getCpuLoad() * 100;
        if (load == 0) load = bean.getCpuLoad() * 100;
        int processors = Runtime.getRuntime().availableProcessors();
        source.sendMessage("%prefix%§7 CPU Usage§8: §6" + format(100, (int) load, "%", false));
        source.sendMessage("%prefix%§7 Running Threads§8: §6" + Thread.activeCount());
        source.sendMessage("%prefix%§7 Processors§8: §6" + processors);
        s = new StringBuilder();
        if (days > 0) s.append(days).append("d ");
        if (hours > 0) s.append(hours % 24).append("h ");
        if (minutes > 0) s.append(minutes % 60).append("m ");
        if (seconds > 0) s.append(seconds % 60).append("s");
        source.sendMessage("%prefix%§7 Uptime§8: §6" + s);
    }

    @Nonnull
    private String format(int max, int used, @Nonnull String digit, boolean storage) {
        float percent = (used * (100f / max));
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            if ((percent / 100) * 20 <= i + 1) s.append("§7|");
            else s.append("§4|");
        }
        if (!storage) return ((int) (used * (100f / max))) + "§8/§6" + max + digit + " §8[" + s + "§8]";
        return "§6" + used + "§8/§6" + max + digit + " §8» §6" + ((int) (used * (100f / max))) + "§8/§6100% §8[" + s + "§8]";
    }

    @Nonnull
    private String format(double tps) {
        double rounded = (double) Math.round(tps * 100.0D) / 100.0D;
        return (rounded >= 18.0D ? "§a" : (rounded >= 16.0D ? "§e" : "§c")) + (rounded >= 20.0D ? "*" : "") + Math.min(rounded, 20.0D);
    }
}
