package net.kapitencraft.kap_lib.client.particle.animation;

import net.minecraft.network.FriendlyByteBuf;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * [WIP]
 */
public class ParticleAnimParam<T> implements Function<FriendlyByteBuf, T>, BiConsumer<FriendlyByteBuf, Object> {
    private static final HashMap<String, ParticleAnimParam<?>> KEYS = new HashMap<>();
    private final String id;
    private final BiConsumer<FriendlyByteBuf, T> serializer;
    private final Function<FriendlyByteBuf, T> deserializer;

    public ParticleAnimParam(String id, BiConsumer<FriendlyByteBuf, T> serializer, Function<FriendlyByteBuf, T> deserializer) {
        this.id = id;
        this.serializer = serializer;
        this.deserializer = deserializer;
        KEYS.put(id, this);
    }

    @Override
    public T apply(FriendlyByteBuf buf) {
        return deserializer.apply(buf);
    }

    @Override
    public void accept(FriendlyByteBuf buf, Object obj) {
        T val = (T) obj;
        serializer.accept(buf, val);
    }

    public static ParticleAnimParam<?> getParam(String name) {
        return KEYS.get(name);
    }
}