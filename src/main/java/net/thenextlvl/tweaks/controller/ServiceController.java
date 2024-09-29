package net.thenextlvl.tweaks.controller;

import lombok.Getter;
import net.thenextlvl.service.api.permission.PermissionController;
import net.thenextlvl.tweaks.TweaksPlugin;

import java.util.Objects;

@Getter
public class ServiceController {
    private final PermissionController permissions;

    public ServiceController(TweaksPlugin plugin) {
        this.permissions = Objects.requireNonNull(plugin.getServer().getServicesManager().load(PermissionController.class));
        plugin.getComponentLogger().info("Using ServiceIO ({}) as permission provider.", permissions.getName());
    }
}
