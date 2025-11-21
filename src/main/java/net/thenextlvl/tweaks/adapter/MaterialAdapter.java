package net.thenextlvl.tweaks.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Type;

@NullMarked
public final class MaterialAdapter implements JsonSerializer<Material>, JsonDeserializer<Material> {
    @Override
    public @Nullable Material deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        return element.isJsonNull() ? null : Material.matchMaterial(element.getAsString());
    }

    @Override
    public JsonElement serialize(@Nullable Material source, Type type, JsonSerializationContext context) {
        return source == null ? JsonNull.INSTANCE : new JsonPrimitive(source.getKey().toString());
    }
}
