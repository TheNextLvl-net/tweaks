package net.thenextlvl.tweaks.controller;

import lombok.Getter;
import net.thenextlvl.service.api.chat.ChatController;
import net.thenextlvl.service.api.economy.EconomyController;
import net.thenextlvl.service.api.economy.bank.BankController;
import net.thenextlvl.service.api.group.GroupController;
import net.thenextlvl.service.api.permission.PermissionController;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@Getter
public class ServiceController {
    private final @Nullable BankController banks;
    private final @Nullable ChatController chat;
    private final @Nullable EconomyController economy;
    private final @Nullable GroupController groups;
    private final @Nullable PermissionController permissions;

    public ServiceController(TweaksPlugin plugin) {
        this.banks = loadController(plugin, BankController.class, BankController::getName, "bank");
        this.chat = loadController(plugin, ChatController.class, ChatController::getName, "chat");
        this.economy = loadController(plugin, EconomyController.class, EconomyController::getName, "economy");
        this.groups = loadController(plugin, GroupController.class, GroupController::getName, "group");
        this.permissions = loadController(plugin, PermissionController.class, PermissionController::getName, "permission");
    }

    private <C> @Nullable C loadController(TweaksPlugin plugin, Class<C> clazz, Function<C, String> name, String description) {
        var controller = plugin.getServer().getServicesManager().load(clazz);
        if (controller != null) plugin.getComponentLogger().info(
                "Using {} as {} provider",
                name.apply(controller), description
        );
        return controller;
    }
}
