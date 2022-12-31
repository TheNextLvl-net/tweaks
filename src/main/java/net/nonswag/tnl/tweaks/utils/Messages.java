package net.nonswag.tnl.tweaks.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.nonswag.core.api.annotation.FieldsAreNonnullByDefault;
import net.nonswag.core.api.file.formats.MessageFile;
import net.nonswag.core.api.language.Language;
import net.nonswag.core.api.message.key.MessageKey;

@FieldsAreNonnullByDefault
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Messages {
    public static final MessageKey TELEPORTED = new MessageKey("teleported").register();
    public static final MessageKey NO_POSITION = new MessageKey("no-position").register();
    public static final MessageKey CLEARED_INVENTORY = new MessageKey("cleared-inventory").register();
    public static final MessageKey CLEARED_OWN_INVENTORY = new MessageKey("cleared-own-inventory").register();
    public static final MessageKey NO_LONGER_OP = new MessageKey("no-longer-op").register();
    public static final MessageKey NOTHING_CHANGED = new MessageKey("nothing-changed").register();
    public static final MessageKey ENCHANTED_ITEM = new MessageKey("enchanted-item").register();
    public static final MessageKey UNENCHANTED_ITEM = new MessageKey("unenchanted-item").register();
    public static final MessageKey HOLD_ITEM = new MessageKey("hold-item").register();
    public static final MessageKey SATISFIED_HUNGER = new MessageKey("satisfied-hunger").register();
    public static final MessageKey SATISFIED_OWN_HUNGER = new MessageKey("satisfied-own-hunger").register();
    public static final MessageKey SELECT_ANOTHER_PLAYER = new MessageKey("select-another-player").register();
    public static final MessageKey NOT_HOLDING_PLAYER_HEAD = new MessageKey("not-holding-player-head").register();
    public static final MessageKey INVALID_VALUE = new MessageKey("invalid-value").register();
    public static final MessageKey INVENTORY_FULL = new MessageKey("inventory-full").register();
    public static final MessageKey RECEIVED_ITEM = new MessageKey("received-item").register();
    public static final MessageKey RECEIVED_ONE_ITEM = new MessageKey("received-one-item").register();
    public static final MessageKey NOTHING_REPAIRED = new MessageKey("not-repaired").register();
    public static final MessageKey NO_DAMAGE = new MessageKey("no-damage").register();
    public static final MessageKey REPAIRED_ITEM = new MessageKey("repaired-item").register();
    public static final MessageKey REPAIRED_MULTIPLE_ITEMS = new MessageKey("repaired-multiple-items").register();
    public static final MessageKey NUMBER_BETWEEN = new MessageKey("number-between").register();
    public static final MessageKey SET_SPEED = new MessageKey("set-speed").register();
    public static final MessageKey NO_OPERATORS = new MessageKey("no-operators").register();
    public static final MessageKey NOW_OPERATOR = new MessageKey("now-operator").register();
    public static final MessageKey HEALED_SELF = new MessageKey("healed-self").register();
    public static final MessageKey HEALED_OTHER = new MessageKey("healed-other").register();
    public static final MessageKey CHANGED_GAMEMODE = new MessageKey("changed-gamemode").register();
    public static final MessageKey NOT_A_PLAYER = new MessageKey("not-a-player").register();

    public static void loadAll() {
        loadEnglish();
        loadGerman();
    }

    private static void loadEnglish() {
        MessageFile english = MessageFile.getOrCreate(Language.AMERICAN_ENGLISH);
        english.setDefault(TELEPORTED, "%prefix% §aYou got teleported");
        english.setDefault(NO_POSITION, "%prefix% §cNo last position set");
        english.setDefault(CLEARED_INVENTORY, "%prefix% §aCleared §6%player%'s§a inventory");
        english.setDefault(CLEARED_OWN_INVENTORY, "%prefix% §aCleared your inventory");
        english.setDefault(NO_LONGER_OP, "%prefix% §6%player%§a is no longer an operator");
        english.setDefault(NOTHING_CHANGED, "%prefix% §cNothing could be changed");
        english.setDefault(ENCHANTED_ITEM, "%prefix% §aEnchanted the item");
        english.setDefault(UNENCHANTED_ITEM, "%prefix% §aUnenchanted the item");
        english.setDefault(HOLD_ITEM, "%prefix% §cHold an item in your hand");
        english.setDefault(SATISFIED_HUNGER, "%prefix% §6%player%'s§a hunger has been satisfied");
        english.setDefault(SATISFIED_OWN_HUNGER, "%prefix% §aYour hunger has been satisfied");
        english.setDefault(SELECT_ANOTHER_PLAYER, "%prefix% §cSelect another player");
        english.setDefault(NOT_HOLDING_PLAYER_HEAD, "%prefix% §cYou are not holding a player head");
        english.setDefault(INVALID_VALUE, "%prefix% §cThe provided value is invalid");
        english.setDefault(INVENTORY_FULL, "%prefix% §cYour inventory is full");
        english.setDefault(RECEIVED_ITEM, "%prefix% §aYou received §8(§7%amount%x§8) §6%item%");
        english.setDefault(RECEIVED_ONE_ITEM, "%prefix% §aYou received a §6%item%");
        english.setDefault(NOTHING_REPAIRED, "%prefix% §cNo items could be repaired");
        english.setDefault(NO_DAMAGE, "%prefix% §cYour item has no damage");
        english.setDefault(REPAIRED_ITEM, "%prefix% §aRepaired your item");
        english.setDefault(REPAIRED_MULTIPLE_ITEMS, "%prefix% §aRepaired §6%amount%§a item(s)");
        english.setDefault(NUMBER_BETWEEN, "%prefix% §cUse a number between §4%first%§c and §4%second%");
        english.setDefault(SET_SPEED, "%prefix% §aSet your §8(§7%mode%§8)§a speed to §6%speed%");
        english.setDefault(NO_OPERATORS, "%prefix% §cThere are no operators");
        english.setDefault(NOW_OPERATOR, "%prefix% §6%player%§a is now an operator");
        english.setDefault(HEALED_SELF, "%prefix% §aYou got healed");
        english.setDefault(HEALED_OTHER, "%prefix% §6%player%§a got healed");
        english.setDefault(CHANGED_GAMEMODE, "%prefix%§a Your gamemode is now §6%gamemode%");
        english.setDefault(NOT_A_PLAYER, "%prefix%§4 %player%§c is not a player");
        english.save();
    }

    private static void loadGerman() {
        MessageFile german = MessageFile.getOrCreate(Language.AMERICAN_ENGLISH);
        german.setDefault(TELEPORTED, "%prefix% §aDu wurdest teleportiert");
        german.setDefault(NO_POSITION, "%prefix% §cKeine letzte position gesetzt");
        german.setDefault(CLEARED_INVENTORY, "%prefix% §6%player%'s§a inventar wurde geleert");
        german.setDefault(CLEARED_OWN_INVENTORY, "%prefix% §aDein inventar wurde geleert");
        german.setDefault(NO_LONGER_OP, "%prefix% §6%player%§a ist jetzt kein operator mehr");
        german.setDefault(NOTHING_CHANGED, "%prefix% §cEs konnte nichts geändert werden");
        german.setDefault(ENCHANTED_ITEM, "%prefix% §aDas item wurde verzaubert");
        german.setDefault(UNENCHANTED_ITEM, "%prefix% §aDas item wurde entzaubert");
        german.setDefault(HOLD_ITEM, "%prefix% §cHalte ein item in deiner hand");
        german.setDefault(SATISFIED_HUNGER, "%prefix% §6%player%'s§a hunger wurde gestillt");
        german.setDefault(SATISFIED_OWN_HUNGER, "%prefix% §aDein hunger wurde gestillt");
        german.setDefault(SELECT_ANOTHER_PLAYER, "%prefix% §cWähle einen anderen spieler");
        german.setDefault(NOT_HOLDING_PLAYER_HEAD, "%prefix% §cDu hältst keinen spieler kopf in der hand");
        german.setDefault(INVALID_VALUE, "%prefix% §cDer angegebene wert ist ungültig");
        german.setDefault(INVENTORY_FULL, "%prefix% §cDein inventar ist voll");
        german.setDefault(RECEIVED_ITEM, "%prefix% §aDu hast §8(§7%amount%x§8) §6%item%§a erhalten");
        german.setDefault(RECEIVED_ONE_ITEM, "%prefix% §aDu hast ein §6%item%§a erhalten");
        german.setDefault(NOTHING_REPAIRED, "%prefix% §cEs wurden keine items repariert");
        german.setDefault(NO_DAMAGE, "%prefix% §cDein item hat keinen schaden");
        german.setDefault(REPAIRED_ITEM, "%prefix% §aDein item wurde repariert");
        german.setDefault(REPAIRED_MULTIPLE_ITEMS, "%prefix% §aEs wurden §6%amount%§a item(s) repariert");
        german.setDefault(NUMBER_BETWEEN, "%prefix% §cNutze eine zahl zwischen §4%first%§c und §4%second%");
        german.setDefault(SET_SPEED, "%prefix% §aDeine §8(§7%mode%§8)§a geschwindigkeit wurde auf §6%speed%§a gesetzt");
        german.setDefault(NO_OPERATORS, "%prefix% §cEs gibt keine operatoren");
        german.setDefault(NOW_OPERATOR, "%prefix% §6%player%§a ist jetzt ein operator");
        german.setDefault(HEALED_SELF, "%prefix% §aDu wurdest geheilt");
        german.setDefault(HEALED_OTHER, "%prefix% §6%player%§a wurde geheilt");
        german.setDefault(CHANGED_GAMEMODE, "%prefix%§a Dein gamemode ist jetzt §6%gamemode%");
        german.setDefault(NOT_A_PLAYER, "%prefix%§4 %player%§c ist kein spieler");
        german.save();
    }
}
