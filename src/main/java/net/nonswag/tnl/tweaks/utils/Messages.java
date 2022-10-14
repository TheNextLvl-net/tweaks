package net.nonswag.tnl.tweaks.utils;

import net.nonswag.core.api.message.Message;
import net.nonswag.core.api.message.key.MessageKey;

import javax.annotation.Nonnull;

public final class Messages {

    @Nonnull
    public static final MessageKey TELEPORTED = new MessageKey("teleported").register();
    @Nonnull
    public static final MessageKey NO_POSITION = new MessageKey("no-position").register();
    @Nonnull
    public static final MessageKey CLEARED_INVENTORY = new MessageKey("cleared-inventory").register();
    @Nonnull
    public static final MessageKey CLEARED_OWN_INVENTORY = new MessageKey("cleared-own-inventory").register();
    @Nonnull
    public static final MessageKey NO_LONGER_OP = new MessageKey("no-longer-op").register();
    @Nonnull
    public static final MessageKey NOTHING_CHANGED = new MessageKey("nothing-changed").register();
    @Nonnull
    public static final MessageKey ENCHANTED_ITEM = new MessageKey("enchanted-item").register();
    @Nonnull
    public static final MessageKey UNENCHANTED_ITEM = new MessageKey("unenchanted-item").register();
    @Nonnull
    public static final MessageKey HOLD_ITEM = new MessageKey("hold-item").register();
    @Nonnull
    public static final MessageKey SATISFIED_HUNGER = new MessageKey("satisfied-hunger").register();
    @Nonnull
    public static final MessageKey SATISFIED_OWN_HUNGER = new MessageKey("satisfied-own-hunger").register();
    @Nonnull
    public static final MessageKey SELECT_ANOTHER_PLAYER = new MessageKey("select-another-player").register();
    @Nonnull
    public static final MessageKey NOT_HOLDING_PLAYER_HEAD = new MessageKey("not-holding-player-head").register();
    @Nonnull
    public static final MessageKey INVALID_VALUE = new MessageKey("invalid-value").register();
    @Nonnull
    public static final MessageKey INVENTORY_FULL = new MessageKey("inventory-full").register();
    @Nonnull
    public static final MessageKey RECEIVED_ITEM = new MessageKey("received-item").register();
    @Nonnull
    public static final MessageKey RECEIVED_ONE_ITEM = new MessageKey("received-one-item").register();
    @Nonnull
    public static final MessageKey NOTHING_REPAIRED = new MessageKey("not-repaired").register();
    @Nonnull
    public static final MessageKey NO_DAMAGE = new MessageKey("no-damage").register();
    @Nonnull
    public static final MessageKey REPAIRED_ITEM = new MessageKey("repaired-item").register();
    @Nonnull
    public static final MessageKey REPAIRED_MULTIPLE_ITEMS = new MessageKey("repaired-multiple-items").register();
    @Nonnull
    public static final MessageKey NUMBER_BETWEEN = new MessageKey("number-between").register();
    @Nonnull
    public static final MessageKey SET_SPEED = new MessageKey("set-speed").register();
    @Nonnull
    public static final MessageKey NO_OPERATORS = new MessageKey("no-operators").register();
    @Nonnull
    public static final MessageKey NOW_OPERATOR = new MessageKey("now-operator").register();
    @Nonnull
    public static final MessageKey HEALED_SELF = new MessageKey("healed-self").register();
    @Nonnull
    public static final MessageKey HEALED_OTHER = new MessageKey("healed-other").register();
    @Nonnull
    public static final MessageKey CHANGED_GAMEMODE = new MessageKey("changed-gamemode").register();
    @Nonnull
    public static final MessageKey NOT_A_PLAYER = new MessageKey("not-a-player").register();

    private Messages() {
    }

    public static void loadAll() {
        loadEnglish();
        loadGerman();
    }

    private static void loadEnglish() {
        Message.getEnglish().setDefault(TELEPORTED, "%prefix% §aYou got teleported");
        Message.getEnglish().setDefault(NO_POSITION, "%prefix% §cNo last position set");
        Message.getEnglish().setDefault(CLEARED_INVENTORY, "%prefix% §aCleared §6%player%'s§a inventory");
        Message.getEnglish().setDefault(CLEARED_OWN_INVENTORY, "%prefix% §aCleared your inventory");
        Message.getEnglish().setDefault(NO_LONGER_OP, "%prefix% §6%player%§a is no longer an operator");
        Message.getEnglish().setDefault(NOTHING_CHANGED, "%prefix% §cNothing could be changed");
        Message.getEnglish().setDefault(ENCHANTED_ITEM, "%prefix% §aEnchanted the item");
        Message.getEnglish().setDefault(UNENCHANTED_ITEM, "%prefix% §aUnenchanted the item");
        Message.getEnglish().setDefault(HOLD_ITEM, "%prefix% §cHold an item in your hand");
        Message.getEnglish().setDefault(SATISFIED_HUNGER, "%prefix% §6%player%'s§a hunger has been satisfied");
        Message.getEnglish().setDefault(SATISFIED_OWN_HUNGER, "%prefix% §aYour hunger has been satisfied");
        Message.getEnglish().setDefault(SELECT_ANOTHER_PLAYER, "%prefix% §cSelect another player");
        Message.getEnglish().setDefault(NOT_HOLDING_PLAYER_HEAD, "%prefix% §cYou are not holding a player head");
        Message.getEnglish().setDefault(INVALID_VALUE, "%prefix% §cThe provided value is invalid");
        Message.getEnglish().setDefault(INVENTORY_FULL, "%prefix% §cYour inventory is full");
        Message.getEnglish().setDefault(RECEIVED_ITEM, "%prefix% §aYou received §8(§7%amount%x§8) §6%item%");
        Message.getEnglish().setDefault(RECEIVED_ONE_ITEM, "%prefix% §aYou received a §6%item%");
        Message.getEnglish().setDefault(NOTHING_REPAIRED, "%prefix% §cNo items could be repaired");
        Message.getEnglish().setDefault(NO_DAMAGE, "%prefix% §cYour item has no damage");
        Message.getEnglish().setDefault(REPAIRED_ITEM, "%prefix% §aRepaired your item");
        Message.getEnglish().setDefault(REPAIRED_MULTIPLE_ITEMS, "%prefix% §aRepaired §6%amount%§a item(s)");
        Message.getEnglish().setDefault(NUMBER_BETWEEN, "%prefix% §cUse a number between §4%first%§c and §4%second%");
        Message.getEnglish().setDefault(SET_SPEED, "%prefix% §aSet your §8(§7%mode%§8)§a speed to §6%speed%");
        Message.getEnglish().setDefault(NO_OPERATORS, "%prefix% §cThere are no operators");
        Message.getEnglish().setDefault(NOW_OPERATOR, "%prefix% §6%player%§a is now an operator");
        Message.getEnglish().setDefault(HEALED_SELF, "%prefix% §aYou got healed");
        Message.getEnglish().setDefault(HEALED_OTHER, "%prefix% §6%player%§a got healed");
        Message.getEnglish().setDefault(CHANGED_GAMEMODE, "%prefix%§a Your gamemode is now §6%gamemode%");
        Message.getEnglish().setDefault(NOT_A_PLAYER, "%prefix%§4 %player%§c is not a player");
        Message.getEnglish().save();
    }

    private static void loadGerman() {
        Message.getGerman().setDefault(TELEPORTED, "%prefix% §aDu wurdest teleportiert");
        Message.getGerman().setDefault(NO_POSITION, "%prefix% §cKeine letzte position gesetzt");
        Message.getGerman().setDefault(CLEARED_INVENTORY, "%prefix% §6%player%'s§a inventar wurde geleert");
        Message.getGerman().setDefault(CLEARED_OWN_INVENTORY, "%prefix% §aDein inventar wurde geleert");
        Message.getGerman().setDefault(NO_LONGER_OP, "%prefix% §6%player%§a ist jetzt kein operator mehr");
        Message.getGerman().setDefault(NOTHING_CHANGED, "%prefix% §cEs konnte nichts geändert werden");
        Message.getGerman().setDefault(ENCHANTED_ITEM, "%prefix% §aDas item wurde verzaubert");
        Message.getGerman().setDefault(UNENCHANTED_ITEM, "%prefix% §aDas item wurde entzaubert");
        Message.getGerman().setDefault(HOLD_ITEM, "%prefix% §cHalte ein item in deiner hand");
        Message.getGerman().setDefault(SATISFIED_HUNGER, "%prefix% §6%player%'s§a hunger wurde gestillt");
        Message.getGerman().setDefault(SATISFIED_OWN_HUNGER, "%prefix% §aDein hunger wurde gestillt");
        Message.getGerman().setDefault(SELECT_ANOTHER_PLAYER, "%prefix% §cWähle einen anderen spieler");
        Message.getGerman().setDefault(NOT_HOLDING_PLAYER_HEAD, "%prefix% §cDu hältst keinen spieler kopf in der hand");
        Message.getGerman().setDefault(INVALID_VALUE, "%prefix% §cDer angegebene wert ist ungültig");
        Message.getGerman().setDefault(INVENTORY_FULL, "%prefix% §cDein inventar ist voll");
        Message.getGerman().setDefault(RECEIVED_ITEM, "%prefix% §aDu hast §8(§7%amount%x§8) §6%item%§a erhalten");
        Message.getGerman().setDefault(RECEIVED_ONE_ITEM, "%prefix% §aDu hast ein §6%item%§a erhalten");
        Message.getGerman().setDefault(NOTHING_REPAIRED, "%prefix% §cEs wurden keine items repariert");
        Message.getGerman().setDefault(NO_DAMAGE, "%prefix% §cDein item hat keinen schaden");
        Message.getGerman().setDefault(REPAIRED_ITEM, "%prefix% §aDein item wurde repariert");
        Message.getGerman().setDefault(REPAIRED_MULTIPLE_ITEMS, "%prefix% §aEs wurden §6%amount%§a item(s) repariert");
        Message.getGerman().setDefault(NUMBER_BETWEEN, "%prefix% §cNutze eine zahl zwischen §4%first%§c und §4%second%");
        Message.getGerman().setDefault(SET_SPEED, "%prefix% §aDeine §8(§7%mode%§8)§a geschwindigkeit wurde auf §6%speed%§a gesetzt");
        Message.getGerman().setDefault(NO_OPERATORS, "%prefix% §cEs gibt keine operatoren");
        Message.getGerman().setDefault(NOW_OPERATOR, "%prefix% §6%player%§a ist jetzt ein operator");
        Message.getGerman().setDefault(HEALED_SELF, "%prefix% §aDu wurdest geheilt");
        Message.getGerman().setDefault(HEALED_OTHER, "%prefix% §6%player%§a wurde geheilt");
        Message.getGerman().setDefault(CHANGED_GAMEMODE, "%prefix%§a Dein gamemode ist jetzt §6%gamemode%");
        Message.getGerman().setDefault(NOT_A_PLAYER, "%prefix%§4 %player%§c ist kein spieler");
        Message.getGerman().save();
    }
}
