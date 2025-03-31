package net.thenextlvl.tweaks.controller;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.thenextlvl.service.api.chat.ChatController;
import net.thenextlvl.service.api.economy.EconomyController;
import net.thenextlvl.service.api.economy.bank.BankController;
import net.thenextlvl.service.api.group.GroupController;
import net.thenextlvl.service.api.group.GroupHolder;
import net.thenextlvl.service.api.permission.PermissionController;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

@NullMarked
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

    public TagResolver serviceResolvers(Player player) {
        var builder = TagResolver.builder();
        Optional.ofNullable(getGroups()).flatMap(groups -> groups.getGroupHolder(player)
                .map(GroupHolder::getPrimaryGroup).flatMap(groups::getGroup)).ifPresent(group -> {
            builder.resolver(Placeholder.parsed("group", group.getDisplayName().orElse(group.getName())));
            group.getPrefix().ifPresent(prefix -> builder.resolver(Placeholder.parsed("group_prefix", prefix)));
            group.getSuffix().ifPresent(suffix -> builder.resolver(Placeholder.parsed("group_suffix", suffix)));
        });
        Optional.ofNullable(getChat()).flatMap(chat -> chat.getProfile(player)).ifPresent(profile -> {
            builder.resolver(Placeholder.parsed("chat_display_name", profile.getDisplayName().orElse(player.getName())));
            profile.getPrefix().ifPresent(prefix -> builder.resolver(Placeholder.parsed("chat_prefix", prefix)));
            profile.getSuffix().ifPresent(suffix -> builder.resolver(Placeholder.parsed("chat_suffix", suffix)));
        });
        Optional.ofNullable(getEconomy()).ifPresent(economy -> {
            economy.getAccount(player).ifPresent(account -> {
                builder.resolver(Placeholder.parsed("balance", economy.format(account.getBalance())));
                builder.resolver(Placeholder.parsed("balance_unformatted", account.getBalance().toString()));
            });
            Optional.ofNullable(banks).flatMap(controller -> controller.getBank(player)).ifPresent(bank -> {
                builder.resolver(Placeholder.parsed("bank_balance", bank.getBalance().toString()));
                builder.resolver(Placeholder.parsed("bank_balance_unformatted", bank.getBalance().toString()));
            });
            builder.resolver(Placeholder.parsed("currency_name", economy.getCurrencyNameSingular(player.locale())));
            builder.resolver(Placeholder.parsed("currency_name_plural", economy.getCurrencyNamePlural(player.locale())));
            builder.resolver(Placeholder.parsed("currency_symbol", economy.getCurrencySymbol()));
        });
        return builder.build();
    }

    public int getChatDeleteWeight(Player player) {
        return Optional.ofNullable(getPermissions())
                .flatMap(controller -> controller.getPermissionHolder(player))
                .flatMap(holder -> holder.intInfoNode("chat-delete-weight"))
                .orElse(-1);
    }

    public int getWeight(Player player) {
        return Optional.ofNullable(getGroups())
                .flatMap(controller -> controller.getGroupHolder(player)
                        .map(GroupHolder::getPrimaryGroup)
                        .flatMap(controller::getGroup))
                .map(group -> group.getWeight().orElse(0))
                .orElse(0);
    }

    public @Nullable BankController getBanks() {
        return banks;
    }

    public @Nullable ChatController getChat() {
        return chat;
    }

    public @Nullable EconomyController getEconomy() {
        return economy;
    }

    public @Nullable GroupController getGroups() {
        return groups;
    }

    public @Nullable PermissionController getPermissions() {
        return permissions;
    }
}
