package net.thenextlvl.tweaks.model;

import net.thenextlvl.i18n.ResourceMigrator;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Locale;
import java.util.Set;

public final class MessageMigrator implements ResourceMigrator {
    private final Set<MigrationRule> rules = Set.of(
            new MigrationRule(Locale.US, "command.last.seen.time", "<time>", "<date:'d MMM uuuu hh:mm:ss a'>"),
            new MigrationRule(Locale.GERMANY, "command.last.seen.time", "<time>", "<date:'MMM d uuuu HH:mm:ss'>")
    );

    @Override
    public @Nullable Migration migrate(@NonNull Locale locale, @NonNull String key, @NonNull String message) {
        return rules.stream().filter(rule -> rule.key().equals(key))
                .filter(rule -> rule.locale().equals(locale))
                .filter(rule -> message.contains(rule.match()))
                .findAny()
                .map(rule -> message.replace(rule.match(), rule.replacement()))
                .map(string -> new Migration(key, string))
                .orElse(null);
    }

    private record MigrationRule(Locale locale, String key, String match, String replacement) {
    }
}
