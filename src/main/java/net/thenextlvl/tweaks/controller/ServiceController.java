package net.thenextlvl.tweaks.controller;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.thenextlvl.service.chat.ChatController;
import net.thenextlvl.service.economy.EconomyController;
import net.thenextlvl.service.economy.bank.BankController;
import net.thenextlvl.service.economy.currency.Currency;
import net.thenextlvl.service.group.GroupController;
import net.thenextlvl.service.group.GroupHolder;
import net.thenextlvl.service.model.MetadataHolder;
import net.thenextlvl.service.permission.PermissionController;
import net.thenextlvl.tweaks.TweaksPlugin;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

@NullMarked
public final class ServiceController {
    private final @Nullable BankController banks;
    private final @Nullable ChatController chat;
    private final @Nullable EconomyController economy;
    private final @Nullable GroupController groups;
    private final @Nullable PermissionController permissions;

    public ServiceController(final TweaksPlugin plugin) {
        this.banks = loadController(plugin, BankController.class, BankController::getName, "bank");
        this.chat = loadController(plugin, ChatController.class, ChatController::getName, "chat");
        this.economy = loadController(plugin, EconomyController.class, EconomyController::getName, "economy");
        this.groups = loadController(plugin, GroupController.class, GroupController::getName, "group");
        this.permissions = loadController(plugin, PermissionController.class, PermissionController::getName, "permission");
    }

    private <C> @Nullable C loadController(final TweaksPlugin plugin, final Class<C> clazz, final Function<C, String> name, final String description) {
        final C controller = plugin.getServer().getServicesManager().load(clazz);
        if (controller != null) plugin.getComponentLogger().info(
                "Using {} as {} provider",
                name.apply(controller), description
        );
        return controller;
    }

    public TagResolver serviceResolvers(final Player player) {
        final var builder = TagResolver.builder();
        Optional.ofNullable(groups).flatMap(groups -> groups.getGroupHolder(player)
                .map(GroupHolder::getPrimaryGroup).flatMap(groups::getGroup)).ifPresent(group -> {
            builder.resolver(Placeholder.parsed("group", group.getDisplayName().orElse(group.getName())));
            group.getPrefix().ifPresent(prefix -> builder.resolver(Placeholder.parsed("group_prefix", prefix)));
            group.getSuffix().ifPresent(suffix -> builder.resolver(Placeholder.parsed("group_suffix", suffix)));
        });
        Optional.ofNullable(chat).flatMap(chat -> chat.getProfile(player)).ifPresent(profile -> {
            builder.resolver(Placeholder.parsed("chat_display_name", profile.getDisplayName().orElse(player.getName())));
            profile.getPrefix().ifPresent(prefix -> builder.resolver(Placeholder.parsed("chat_prefix", prefix)));
            profile.getSuffix().ifPresent(suffix -> builder.resolver(Placeholder.parsed("chat_suffix", suffix)));
        });
        // todo: add proper placeholder support
        Optional.ofNullable(economy).ifPresent(economy -> {
            final var currency = economy.getCurrencyController().getDefaultCurrency();
            economy.getAccount(player).ifPresent(account -> {
                builder.resolver(Placeholder.component("balance", currency.format(account.getBalance(currency), player)));
                builder.resolver(Placeholder.parsed("balance_unformatted", account.getBalance(currency).toString()));
            });
            registerCurrencyTags(player, currency, builder);
        });
        Optional.ofNullable(banks).ifPresent(banks -> {
            final var currency = banks.getCurrencyController().getDefaultCurrency();
            banks.getBank(player).ifPresent(account -> {
                builder.resolver(Placeholder.component("bank_balance", currency.format(account.getBalance(currency), player)));
                builder.resolver(Placeholder.parsed("bank_balance_unformatted", account.getBalance(currency).toString()));
            });
            if (economy == null) registerCurrencyTags(player, currency, builder);
        });
        return builder.build();
    }

    private static void registerCurrencyTags(final Player player, final Currency currency, final TagResolver.Builder builder) {
        currency.getDisplayNameSingular(player).ifPresent(name -> builder.resolver(Placeholder.component("currency_name", name)));
        currency.getDisplayNamePlural(player).ifPresent(name -> builder.resolver(Placeholder.component("currency_name_plural", name)));
        builder.resolver(Placeholder.component("currency_symbol", currency.getSymbol()));
    }

    public int getChatDeleteWeight(final Player player) {
        return Optional.ofNullable(permissions)
                .flatMap(controller -> controller.getPermissionHolder(player))
                .filter(holder -> holder instanceof MetadataHolder)
                .map(holder -> (MetadataHolder) holder)
                .flatMap(holder -> holder.intInfoNode("chat-delete-weight"))
                .orElse(-1);
    }

    public int getWeight(final Player player) {
        return Optional.ofNullable(groups)
                .flatMap(controller -> controller.getGroupHolder(player)
                        .map(GroupHolder::getPrimaryGroup)
                        .flatMap(controller::getGroup))
                .map(group -> group.getWeight().orElse(0))
                .orElse(0);
    }

    public Optional<Integer> getMaxHomeCount(final Player player) {
        return Optional.ofNullable(permissions)
                .flatMap(controller -> controller.getPermissionHolder(player))
                .filter(holder -> holder instanceof MetadataHolder)
                .map(holder -> (MetadataHolder) holder)
                .flatMap(permissionHolder -> permissionHolder.intInfoNode("max-homes"));
    }
}
