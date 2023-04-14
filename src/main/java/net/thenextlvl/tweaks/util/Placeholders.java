package net.thenextlvl.tweaks.util;

import core.api.placeholder.Placeholder;
import net.thenextlvl.tweaks.TweaksPlugin;

public class Placeholders {
    public static void init(TweaksPlugin plugin) {
        plugin.formatter().registry().register(Placeholder.of("prefix", Messages.PREFIX.message()));
    }
}
