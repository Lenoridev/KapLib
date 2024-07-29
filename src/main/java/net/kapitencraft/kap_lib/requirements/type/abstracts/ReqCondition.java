package net.kapitencraft.kap_lib.requirements.type.abstracts;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import net.kapitencraft.kap_lib.registry.custom.ModRegistries;
import net.kapitencraft.kap_lib.util.serialization.JsonSerializer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public abstract class ReqCondition<T extends ReqCondition<T>> {

    public abstract boolean matches(Player player);

    public abstract Codec<T> getCodec();

    public abstract Component display();

    //data-gen
    public static <T extends ReqCondition<T>> ReqCondition<T> readFromJson(JsonObject object) {
        Codec<T> codec = (Codec<T>) ModRegistries.REQUIREMENT_TYPE.getValue(new ResourceLocation(GsonHelper.getAsString(object, "type")));
        if (codec == null) throw new NullPointerException("unknown requirement type: '" + GsonHelper.getAsString(object, "type") + "'");
        JsonSerializer<T> serializer = new JsonSerializer<>(codec);
        return serializer.deserialize(object);
    }

    public JsonElement saveToJson() {
        JsonSerializer<T> serializer = new JsonSerializer<>(getCodec());
        JsonObject object = (JsonObject) serializer.serialize((T) this);
        object.addProperty("type", Objects.requireNonNull(ModRegistries.REQUIREMENT_TYPE.getKey(this.getCodec()), "unknown requirement type: " + this.getClass().getCanonicalName()).toString());
        return object;
    }
}