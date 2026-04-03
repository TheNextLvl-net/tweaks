package net.thenextlvl.tweaks.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.kyori.adventure.key.Key;
import net.thenextlvl.tweaks.model.LazyLocation;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Type;

@NullMarked
public final class LazyLocationAdapter implements JsonSerializer<LazyLocation>, JsonDeserializer<LazyLocation> {
    @Override
    @SuppressWarnings("PatternValidation")
    public @Nullable LazyLocation deserialize(final JsonElement element, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        if (!element.isJsonPrimitive()) return null;
        final var split = element.getAsString().split(", ");
        final var world = Key.key(split[0]);
        final var x = Double.parseDouble(split[1]);
        final var y = Double.parseDouble(split[2]);
        final var z = Double.parseDouble(split[3]);
        final var yaw = split.length == 6 ? Float.parseFloat(split[4]) : 0;
        final var pitch = split.length == 6 ? Float.parseFloat(split[5]) : 0;
        return new LazyLocation(world, x, y, z, yaw, pitch);
    }

    @Override
    public JsonElement serialize(@Nullable final LazyLocation source, final Type type, final JsonSerializationContext context) {
        if (source == null) return JsonNull.INSTANCE;
        final var builder = new StringBuilder(source.key().asString()).append(", ")
                .append(source.getX()).append(", ")
                .append(source.getY()).append(", ")
                .append(source.getZ());
        if (source.getYaw() != 0 || source.getPitch() != 0)
            builder.append(", ").append(source.getYaw()).append(", ").append(source.getPitch());
        return new JsonPrimitive(builder.toString());
    }
}
