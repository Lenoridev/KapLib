package net.kapitencraft.kap_lib.requirements.type.abstracts;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import net.kapitencraft.kap_lib.io.serialization.DataGenSerializer;
import net.kapitencraft.kap_lib.registry.custom.core.ModRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class ReqCondition<T extends ReqCondition<T>> {
    private Component displayCache;

    public static <T extends ReqCondition<T>> DataGenSerializer<T> createSerializer(Codec<T> codec, FriendlyByteBuf.Reader<T> factory) {
        return new DataGenSerializer<>(codec, factory, (buf, t) -> t.toNetwork(buf));
    }

    protected ReqCondition() {
    }

    public abstract boolean matches(Player player);

    public abstract DataGenSerializer<T> getSerializer();

    protected abstract @NotNull Component cacheDisplay();

    public @NotNull Component display() {
        return displayCache == null ? displayCache = cacheDisplay() : displayCache;
    }

    //data-gen
    public static <T extends ReqCondition<T>> ReqCondition<T> readFromJson(JsonObject object) {
        DataGenSerializer<T> serializer = (DataGenSerializer<T>) ModRegistries.REQUIREMENT_TYPE.getValue(new ResourceLocation(GsonHelper.getAsString(object, "type")));
        if (serializer == null) throw new NullPointerException("unknown requirement type: '" + GsonHelper.getAsString(object, "type") + "'");
        return serializer.deserialize(object);
    }

    public JsonElement saveToJson() {
        JsonObject object = (JsonObject) getSerializer().serialize((T) this);
        object.addProperty("type", Objects.requireNonNull(ModRegistries.REQUIREMENT_TYPE.getKey(this.getSerializer()), "unknown requirement type: " + this.getClass().getCanonicalName()).toString());
        return object;
    }

    //network
    public void toNetwork(FriendlyByteBuf byteBuf) {
        byteBuf.writeRegistryId(ModRegistries.REQUIREMENT_TYPE, this.getSerializer());
        additionalToNetwork(byteBuf);
    }

    protected abstract void additionalToNetwork(FriendlyByteBuf buf);

    public static <T extends ReqCondition<T>> ReqCondition<T> fromNetwork(FriendlyByteBuf buf) {
        DataGenSerializer<T> serializer = buf.readRegistryId();
        return serializer.fromNetwork(buf);
    }
}