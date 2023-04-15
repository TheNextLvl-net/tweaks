package net.thenextlvl.tweaks.command.api;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Locale;

public class NoPermissionException extends CommandException {

    private final String permission;


    public NoPermissionException(String permission) {
        this.permission = permission;
    }

    @Override
    protected void handle(Locale locale, CommandSender sender, Command command) {
        Component commandPermissionMessage = command.permissionMessage();
        Component message = commandPermissionMessage != null ? commandPermissionMessage : Bukkit.permissionMessage();
        if (!Component.empty().equals(message)) {
            sender.sendMessage(message.replaceText(net.kyori.adventure.text.TextReplacementConfig.builder().matchLiteral("<permission>").replacement(permission).build()));
        }
    }
}
